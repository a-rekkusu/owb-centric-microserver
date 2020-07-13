package org.apache.peeco.showcase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.peeco.api.HttpServer;

@ApplicationScoped
public class HttpServerProducer
{
    @Produces
    @ApplicationScoped
    public HttpServer init()
    {
        return new HttpServer.Builder()
                .port(8080)
                .ssl(false)
                .host("localhost")
                .build();
    }
}
