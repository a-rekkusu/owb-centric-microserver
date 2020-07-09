package org.apache.peeco.impl;

import java.net.SocketAddress;

public class Configuration
{
    private int httpPort = 8080;
    private boolean ssl;
    private String host = "localhost";

    public Configuration(Configuration toCopy)
    {
        this.httpPort = toCopy.httpPort;
        this.ssl = toCopy.ssl;
        this.host = toCopy.host;
    }

    public Configuration()
    {

    }

    public Configuration setHttpPort(int port)
    {
        this.httpPort = port;
        return this;
    }

    public int getHttpPort()
    {
        return httpPort;
    }

    public boolean isSsl()
    {
        return Boolean.parseBoolean(System.getProperty("ssl"));
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String address)
    {
        this.host = address;
    }
}
