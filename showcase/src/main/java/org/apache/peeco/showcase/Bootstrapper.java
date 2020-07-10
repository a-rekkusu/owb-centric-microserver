package org.apache.peeco.showcase;

import org.apache.peeco.impl.HttpServer;

public class Bootstrapper
{
    public static void main(String[] args) throws Exception
    {
        HttpServer.Builder builder = new HttpServer.Builder();
        builder.setHttpPort(9999);

        try (HttpServer server = new HttpServer(builder))
        {
            server.bootstrap();
        }
    }

}
