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
package org.apache.peeco.api;

public class HttpServer
{


    private int port;
    private boolean ssl;
    private String host;

    protected HttpServer()
    {
    }

    private HttpServer(Builder builder)
    {
        this.port = builder.port;
        this.ssl = builder.ssl;
        this.host = builder.host;
    }

    public void setPort(int port)
    {
        this.port = this.port == 0 ? port : this.port;
    }

    public int getPort()
    {
        return port;
    }

    public boolean isSsl()
    {
        return ssl;
    }

    public String getHost()
    {
        return host;
    }

    public static class Builder
    {
        private int port = 8080;
        private boolean ssl = false;
        private String host = "localhost";

        public Builder()
        {
        }

        Builder(int port, boolean ssl, String host)
        {
            this.port = port;
            this.ssl = ssl;
            this.host = host;
        }

        public Builder port(int port)
        {
            this.port = port;
            return Builder.this;
        }

        public Builder ssl(boolean ssl)
        {
            this.ssl = ssl;
            return Builder.this;
        }

        public Builder host(String host)
        {
            this.host = host;
            return Builder.this;
        }

        public HttpServer build()
        {

            return new HttpServer(this);
        }
    }
}