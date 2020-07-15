package org.apache.peeco.api;

public class HttpServer
{

    private int configuredPort;
    private int runtimePort;
    private boolean ssl;
    private String host;

    public int getRuntimePort()
    {
        return runtimePort;
    }

    public void setRuntimePort(int runtimePort)
    {
        //must only be set once from impl
        this.runtimePort = this.runtimePort == 0 ? runtimePort : throw_();
    }

    public int throw_(){
        throw new RuntimeException("runtimePort is already set to " + this.runtimePort);
    }

    protected HttpServer()
    {
    }

    private HttpServer(Builder builder)
    {
        this.configuredPort = builder.port;
        this.ssl = builder.ssl;
        this.host = builder.host;
    }

    public int getConfiguredPort()
    {
        return configuredPort;
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