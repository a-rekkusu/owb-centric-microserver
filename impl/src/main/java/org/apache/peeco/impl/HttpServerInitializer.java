package org.apache.peeco.impl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;

import java.util.List;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
{
    private final SslContext sslCtx;
    private List<HttpHandlerInfo> httpHandlerInfos;

    public HttpServerInitializer(SslContext sslCtx, List<HttpHandlerInfo> httpHandlerInfos) {
        this.sslCtx = sslCtx;
        this.httpHandlerInfos = httpHandlerInfos;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (this.sslCtx != null) {
            p.addLast(this.sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpServerExpectContinueHandler());
        p.addLast("aggregator", new HttpObjectAggregator(Short.MAX_VALUE));
        p.addLast(new HttpServerHandler(httpHandlerInfos));
    }
}
