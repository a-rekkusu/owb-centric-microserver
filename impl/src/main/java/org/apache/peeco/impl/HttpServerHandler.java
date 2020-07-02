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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.peeco.api.Request;
import org.apache.peeco.api.Response;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
    
    private List<HttpHandlerInfo> httpHandlerInfos;

    public HttpServerHandler(List<HttpHandlerInfo> httpHandlerInfos)
    {
        this.httpHandlerInfos = httpHandlerInfos;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
    {
        if (msg instanceof HttpRequest)
        {
            HttpRequest nettyRequest = (HttpRequest) msg;
            httpRequestLogger(nettyRequest);
            
            HttpHandlerInfo info = HttpHandlerUtils.getMatchingHandler(nettyRequest, httpHandlerInfos);
            if (info == null)
            {
                // TODO ignore? throw exception? dont know
            }

            Request request = new Request(HttpHandlerUtils.mapHttpMethod(nettyRequest.method()), nettyRequest.uri(), null);

            // TODO parse queryParams from netty and add in our request
            // TODO parse headers from netty and add in our request
            parseBodyParams(nettyRequest, request);

            
            
            Object handlerParentBean = CDI.current().select(info.clazz).get();

            try
            {
                Object response = info.method.invoke(handlerParentBean, request);

                if (response instanceof Response)
                {
                    Response peecoResponse = (Response) response;

                    ByteBuf nettyBuffer = ctx.alloc().buffer(peecoResponse.output().available());
                    
                    nettyBuffer.writeBytes(peecoResponse.output(), peecoResponse.output().available());
                    
                    FullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                            nettyRequest.protocolVersion(),
                            HttpResponseStatus.OK,
                            nettyBuffer);

                    for (Map.Entry<String, List<String>> headers : peecoResponse.headers().entrySet())
                    {
                        nettyResponse.headers().add(headers.getKey(), headers.getValue());
                    }
                    
                    nettyResponse.headers()
                            .setInt(HttpHeaderNames.CONTENT_LENGTH, nettyResponse.content().readableBytes());
                    ChannelFuture f = ctx.write(nettyResponse);
                }
                else if (response instanceof CompletionStage)
                {
                    // TODO impl
                }                
            }
            catch (Exception ex)
            {
                // TODO exception handling
                ex.printStackTrace();
            }   
        }
    }

    
    protected void parseBodyParams(HttpRequest nettyRequest, Request request)
    {
        if (nettyRequest.method().equals(HttpMethod.POST))
        {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, nettyRequest);
            for (InterfaceHttpData data : decoder.getBodyHttpDatas())
            {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                {
                    Attribute attr = (Attribute) data;
                    List<String> values = request.bodyParameters().computeIfAbsent(data.getName(), k -> new ArrayList<>());

                    // TODO nicer eception handling
                    try {
                        values.add(attr.getValue());
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
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
