package nettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;

public class HelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject>
{
    public HelloWorldServerHandler()
    {
    }

    private final StringBuilder responseContent = new StringBuilder();
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
    private HttpPostRequestDecoder decoder;
    private HttpRequest request;


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
                .append("<form action=\"post\" method=\"POST\">\r\n")
                .append("Enter your name: \r\n")
                .append("<input type=\"text\" name=\"user\" />\r\n")
                .append("<input type=\"submit\" value=\"Submit\" />\r\n")
                .append("</form>\r\n")
                .append("</body></html>\r\n");

        ByteBuf buffer = ctx.alloc().buffer(responseContent.length());
        buffer.writeCharSequence(responseContent.toString(), CharsetUtil.UTF_8);

        return buffer;
    }

    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
    {

        if (msg instanceof HttpRequest)
        {
            request = (HttpRequest) msg;

            //GET Response
            if (request.method().equals(HttpMethod.GET))
            {
                httpRequestLogger(request);

                FullHttpResponse response = new DefaultFullHttpResponse(
                        request.protocolVersion(),
                        HttpResponseStatus.OK,
                        writeGetResponse(ctx));

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML)
                        .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                ChannelFuture f = ctx.write(response);
            }

            //POST Response
            else if (request.method().equals(HttpMethod.POST))
            {
                httpRequestLogger(request);

                FullHttpResponse response = new DefaultFullHttpResponse(
                        request.protocolVersion(),
                        HttpResponseStatus.OK,
                        writePostResponse(ctx));

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                        .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                ChannelFuture f = ctx.write(response);
            }
        }
    }

    public void httpRequestLogger(HttpRequest req)
    {
        System.out.println("Request received:");
        System.out.println("HTTP Method: " + req.method());
        System.out.println("HTTP Version: " + req.protocolVersion());
        System.out.println("Headers: " + req.headers());
    }
}

