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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.peeco.api.HttpHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.*;

import org.apache.peeco.api.HttpServer;

public class PeecoExtension implements Extension
{
    private static final Logger logger = LogManager.getLogger();
    private List<HttpHandlerInfo> httpHandlerInfos = new ArrayList<>();

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent) throws Exception
    {
        logger.log(Level.INFO, "----PROCESS ANNOTATED TYPE----");

        List<HttpHandlerInfo> infos = PeecoUtils.collectInfos(patEvent.getAnnotatedType().getJavaClass());

        for (HttpHandlerInfo info : infos)
        {
            logger.log(Level.INFO, "Valid HttpHandler found: Class: " + info.clazz +
                    ", Method: " + info.method +
                    ", Annotation: " + info.annotation.url() + ", " + Arrays.toString(info.annotation.method()) + ", " + info.annotation.matching());
        }

        httpHandlerInfos.addAll(infos);
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv, HttpServer httpServer) throws Exception
    {
        for (HttpHandlerInfo info : httpHandlerInfos)
        {
            info.bean = (CDI.current().select(info.clazz).get());
        }

        logger.log(Level.INFO, "----AFTER DEPLOYMENT VALIDATION----");

        SslContext sslCtx = null;
        if (httpServer.isSsl())
        {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
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
                    .childHandler(new PeecoChannelInitializer(sslCtx, httpHandlerInfos));
            Channel ch = b.bind(httpServer.getPort()).sync().channel();

            //TODO set host in Configuration

            logger.log(Level.INFO, "Peeco started successfully on " + (httpServer.isSsl() ? "https" : "http") + "://127.0.0.1:" + httpServer.getPort() + '/');
            ch.closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public List<HttpHandlerInfo> getHttpHandlerInfos()
    {
        return httpHandlerInfos;
    }
}