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

import java.util.List;

public class HttpServer
{
    private final Configuration configuration;
    private List<HttpHandlerInfo> httpHandlerInfos;

    public HttpServer(List<HttpHandlerInfo> httpHandlerInfos) throws Exception
    {
        this(httpHandlerInfos, new HttpServer.Builder());
    }

    public HttpServer(List<HttpHandlerInfo> httpHandlerInfos, Configuration configuration){
        this.httpHandlerInfos = httpHandlerInfos;
        this.configuration = configuration;
    }

    public void bootstrap() throws Exception
    {
        SslContext sslCtx;
        if (configuration.isSsl())
        {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }
        else
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
                    .childHandler(new HttpServerInitializer(sslCtx, httpHandlerInfos));
            Channel ch = b.bind(configuration.getHttpPort()).sync().channel();
            configuration.setHost(ch.remoteAddress().toString());
            System.err.println("Open your web browser and navigate to " + (configuration.isSsl() ? "https" : "http") + "://127.0.0.1:" + configuration.getHttpPort() + '/');
            ch.closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public int getPort()
    {
        return configuration.getHttpPort();
    }

    public boolean isSSL()
    {
        return configuration.isSsl();
    }

    public String getHost(){
        return configuration.getHost();
    }

    public List<HttpHandlerInfo> getHttpHandlerInfos(){
        return httpHandlerInfos;
    }

    public static class Builder extends Configuration
    {
        public Builder()
        {
        }

        public Builder(Configuration configuration) {
            super(configuration);
        }
    }

}


