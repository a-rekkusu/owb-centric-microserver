package org.apache.peeco.showcase;

import org.apache.peeco.impl.HttpHandlerInfo;
import org.apache.peeco.impl.HttpServer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ServerUser
{
    @Inject
    private HttpServer server;

    private String target = "http://localhost:" + server.getPort();
    private Boolean ssl = server.isSSL();
    private String host = server.getHost();
    private List<HttpHandlerInfo> httpHandlerInfos = server.getHttpHandlerInfos();
}
