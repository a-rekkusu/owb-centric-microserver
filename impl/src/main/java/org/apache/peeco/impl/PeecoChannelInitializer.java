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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;

import java.util.List;

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