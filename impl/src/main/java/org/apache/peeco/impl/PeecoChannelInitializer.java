package org.apache.peeco.impl;

import org.apache.peeco.impl.PeecoChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;

import java.util.List;
import org.apache.peeco.impl.HttpHandlerInfo;

public class PeecoChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final SslContext sslCtx;
    private List<HttpHandlerInfo> httpHandlerInfos;

    public PeecoChannelInitializer(SslContext sslCtx, List<HttpHandlerInfo> httpHandlerInfos)
    {
        this.sslCtx = sslCtx;
        this.httpHandlerInfos = httpHandlerInfos;
    }

    @Override
    public void initChannel(SocketChannel ch)
    {
        ChannelPipeline p = ch.pipeline();
        if (this.sslCtx != null)
        {
            p.addLast(this.sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpServerExpectContinueHandler());
        p.addLast("aggregator", new HttpObjectAggregator(Short.MAX_VALUE));
        p.addLast(new PeecoChannelHandler(httpHandlerInfos));
    }
}
