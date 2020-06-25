package org.apache.peeco.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.List;

public class HttpServer
{

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT;
    private List<HttpHandlerInfo> httpHandlerInfos;
    private BeanManager beanManager = CDI.current().getBeanManager();

    public HttpServer(List<HttpHandlerInfo> httpHandlerInfos) throws Exception
    {
        this.httpHandlerInfos = httpHandlerInfos;
    }

    public void bootstrap() throws Exception
    {
        SslContext sslCtx;
        if (SSL)
        {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else
        {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(sslCtx));
            Channel ch = b.bind(PORT ).sync().channel();
            System.err.println("Open your web browser and navigate to " + (SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
            ch.closeFuture().sync();
        } finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static
    {
        PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
    }

    public void registerHandlers(List<HttpHandlerInfo> httpHandlerInfos)
    {

    }
}


