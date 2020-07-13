package org.apache.peeco.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;

import javax.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.apache.peeco.api.Request;
import org.apache.peeco.api.Response;

public class PeecoChannelHandler extends SimpleChannelInboundHandler<HttpObject>
{
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);

    private List<HttpHandlerInfo> httpHandlerInfos;

    public PeecoChannelHandler(List<HttpHandlerInfo> httpHandlerInfos)
    {
        this.httpHandlerInfos = httpHandlerInfos;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
    {
        if (msg instanceof HttpRequest)
        {
            // TODO: start requestScoped
            
            HttpRequest nettyRequest = (HttpRequest) msg;
            httpRequestLogger(nettyRequest);

            HttpHandlerInfo info = PeecoUtils.getMatchingHandler(nettyRequest, httpHandlerInfos);

            if (info == null)
            {
                throw new Exception("No matching HttpHandler found for incoming URI from Netty Request: " + nettyRequest.uri());
            }

            Request request = new Request(PeecoUtils.mapHttpMethod(nettyRequest.method()), nettyRequest.uri(), null);

            parseHeaders(nettyRequest, request);
            parseQueryParams(nettyRequest, request);
            parseBodyParams(nettyRequest, request);

            // TODO use Info.bean
            Object handlerParentBean = CDI.current().select(info.clazz).get();

            try
            {
                Object returnValue = info.method.invoke(handlerParentBean, request);

                if (returnValue instanceof Response)
                {
                    Response response = (Response) returnValue;

                    ctx.write(createNettyResponse(ctx, response, nettyRequest), ctx.voidPromise());
                }
                else if (returnValue instanceof CompletionStage)
                {
                    CompletionStage<Response> completionStageResponse = (CompletionStage<Response>) returnValue;

                    completionStageResponse.thenAccept(response ->
                    {
                        try
                        {
                            ctx.write(createNettyResponse(ctx, response, nettyRequest))
                                    .addListener((ChannelFutureListener) channelFuture ->
                                            System.out.println(channelFuture.toString() + " IS DONE!"));
                        }
                        catch (IOException ex)
                        {
                            //redundant catch as it's caught in createNettyResponse() already, but IDE requires to catch it here again
                        }
                    });
                }
            }
            catch (Exception ex)
            {
                throw new RuntimeException("Failed to create Netty Response from given HttpHandler Response object, Netty ChannelHandlerContext and Netty Request.");
            }
            
            // TODO stop requestScoped in finally block
        }
    }

    public FullHttpResponse createNettyResponse(ChannelHandlerContext ctx, Response response, HttpRequest nettyRequest) throws IOException
    {

        ByteBuf nettyBuffer = ctx.alloc().buffer(response.output().available());

        try
        {
            nettyBuffer.writeBytes(response.output(), response.output().available());
        }
        catch (IOException ex)
        {
            throw new IOException("Response output from HttpHandler could not be written to Netty ByteBuffer.");
        }

        FullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                nettyRequest.protocolVersion(),
                HttpResponseStatus.OK,
                nettyBuffer);

        for (Map.Entry<String, List<String>> headers : response.headers().entrySet())
        {
            nettyResponse.headers().add(headers.getKey(), headers.getValue());
        }

        nettyResponse.headers()
                .setInt(HttpHeaderNames.CONTENT_LENGTH, nettyResponse.content().readableBytes());

        return nettyResponse;
    }

    protected void parseHeaders(HttpRequest nettyRequest, Request request)
    {
        for (Map.Entry<String, String> headers : nettyRequest.headers())
        {
            List<String> values = request.headers().computeIfAbsent(headers.getKey(), k -> new ArrayList<>());

            List<String> nettyHeaderValues = Arrays.asList(headers.getValue().split(","));

            for (String value : nettyHeaderValues)
            {
                values.add(value);
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

                    try
                    {
                        values.add(attr.getValue());
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException("Failed to parse attribute values from Netty Request body to " + Request.class.toString());
                    }
                }
            }
            decoder.destroy();
        }
    }

    protected void parseQueryParams(HttpRequest nettyRequest, Request request)
    {
        QueryStringDecoder decoder = new QueryStringDecoder(nettyRequest.uri());

        for (Map.Entry<String, List<String>> queryParameters : decoder.parameters().entrySet())
        {
            List<String> values = request.queryParameters().computeIfAbsent(queryParameters.getKey(), k -> new ArrayList<>());

            for (String value : queryParameters.getValue())
            {
                values.add(value);
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
