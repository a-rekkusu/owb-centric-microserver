/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.peeco.api.HttpHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.*;

import org.apache.peeco.api.HttpServer;

public class PeecoExtension implements Extension
{
    private static final Logger logger = Logger.getLogger(PeecoExtension.class.getName());
    private static final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
    private static boolean enabled = true;

    private List<HttpHandlerInfo> httpHandlerInfos = new ArrayList<>();

    public static void disable()
    {
        enabled = false;
    }

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent) throws Exception
    {
        if (!enabled)
        {
            return;
        }

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
        if (!enabled)
        {
            return;
        }

        for (HttpHandlerInfo info : httpHandlerInfos)
        {
            info.bean = CDI.current().select(info.clazz).get();
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
                    .handler(loggingHandler)
                    .childHandler(new PeecoChannelInitializer(sslCtx, httpHandlerInfos));
            Channel ch = b.bind(httpServer.getPort()).sync().channel();

            String[] localAddressSplitted = ch.localAddress().toString().split(":");
            String port = localAddressSplitted[localAddressSplitted.length - 1];
            System.out.println(port);
            httpServer.setPort(Integer.parseInt(port));

            //TODO set host in Configuration

            logger.log(Level.INFO, "Peeco started successfully on " + (httpServer.isSsl() ? "https" : "http") +
                    "://127.0.0.1:" + httpServer.getPort() + '/');
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