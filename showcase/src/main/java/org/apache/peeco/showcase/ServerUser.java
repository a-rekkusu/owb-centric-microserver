package org.apache.peeco.showcase;

import org.apache.peeco.impl.HttpHandlerInfo;
import org.apache.peeco.impl.HttpServer;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ServerUser
{
    private HttpServer server;

    String target = "http://localhost:" + server.getPort();
    Boolean ssl = server.isSSL();
    String host = server.getHost();
    List<HttpHandlerInfo> httpHandlerInfos = server.getHttpHandlerInfos();
}
