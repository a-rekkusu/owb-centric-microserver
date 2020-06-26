package org.apache.peeco.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.List;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{
    public HttpServerHandler(List<HttpHandlerInfo> httpHandlerInfos)
    {
        this.httpHandlerInfos = httpHandlerInfos;
    }

    private List<HttpHandlerInfo> httpHandlerInfos;
    private final StringBuilder responseContent = new StringBuilder();
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
    private HttpPostRequestDecoder decoder;
    private HttpRequest request;
    private BeanManager beanManager = CDI.current().getBeanManager();

    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
    {
        if (msg instanceof HttpRequest)
        {
            request = (HttpRequest) msg;
            httpRequestLogger(request);

            if (uri_matching(request))
            {
                if (request.method().equals(HttpMethod.GET))
                {
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            request.protocolVersion(),
                            HttpResponseStatus.OK,
                            writeGetResponse(ctx));

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML)
                            .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                    ChannelFuture f = ctx.write(response);
                }

                else if (request.method().equals(HttpMethod.POST))
                {
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            request.protocolVersion(),
                            HttpResponseStatus.OK,
                            writePostResponse(ctx));

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML)
                            .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                    ChannelFuture f = ctx.write(response);
                }
            }
        }
    }

    public boolean uri_matching(HttpRequest request)
    {
        boolean uri_matching = false;
        for (HttpHandlerInfo info : httpHandlerInfos)
        {
            if (request.uri().equals(info.values.url))
            {
                return true;
            }
        }
        return uri_matching;
    }

    private ByteBuf writePostResponse(ChannelHandlerContext ctx)
    {
        decoder = new HttpPostRequestDecoder(factory, request);
        InterfaceHttpData data = decoder.getBodyHttpData("user");

        System.out.println("http data type (expected: attribute) : " + data.getHttpDataType());

        Attribute attribute = (Attribute) data;

        try
        {
            String user = attribute.getValue();

            responseContent.setLength(0);
            responseContent.append("<!DOCTYPE html>\r\n")
                    .append("<html><head><meta charset='utf-8' /><title>")
                    .append("Welcome title")
                    .append("</title></head><body>\r\n")
                    .append("Echo from Netty: welcome " + user + "!\r\n")
                    .append("</body></html>\r\n");
        } catch (IOException e)
        {
            e.printStackTrace();
            responseContent.setLength(0);
            responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " Error while reading value: " + e.getMessage() + "\r\n");
        }

        ByteBuf buffer = ctx.alloc().buffer(responseContent.length());
        buffer.writeCharSequence(responseContent.toString(), CharsetUtil.UTF_8);
        decoder.destroy();

        return buffer;
    }

    private ByteBuf writeGetResponse(ChannelHandlerContext ctx)
    {
        responseContent.setLength(0);
        responseContent.append("<!DOCTYPE html>\r\n")
                .append("<html><head><meta charset='utf-8' /><title>")
                .append("ruby yacht poet gang")
                .append("</title></head><body>\r\n")
                .append("<form method=\"POST\">\r\n")
                .append("Enter your name: \r\n")
                .append("<input type=\"text\" name=\"user\" />\r\n")
                .append("<input type=\"submit\" value=\"Submit\" />\r\n")
                .append("</form>\r\n")
                .append("</body></html>\r\n");

        ByteBuf buffer = ctx.alloc().buffer(responseContent.length());
        buffer.writeCharSequence(responseContent.toString(), CharsetUtil.UTF_8);

        return buffer;
    }

    public void httpRequestLogger(HttpRequest req)
    {
        System.out.println("Request received:");
        System.out.println("HTTP Method: " + req.method());
        System.out.println("URI: " + req.uri());
        System.out.println("HTTP Version: " + req.protocolVersion());
        System.out.println("Headers: " + req.headers());
    }
}
