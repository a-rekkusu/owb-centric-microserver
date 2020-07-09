package org.apache.peeco.showcase;

import com.sun.org.apache.xpath.internal.operations.And;
import org.apache.peeco.impl.HttpServer;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Bootstrapper
{
    public static void main(String[] args) throws Exception
    {
        HttpServer.Builder builder = new HttpServer.Builder();
        builder.setHttpPort(9999);

        try (HttpServer server = new HttpServer(builder))
        {
            //TODO requests throw exceptions now: No CDI Container started
            server.bootstrap();
        }
    }

}
