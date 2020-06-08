package handlers;

import bindings.HttpHandler;
import enums.Matching;
import enums.Method;
import nettyComponents.HelloWorldServer;

import javax.inject.Inject;

@HttpHandler(method = {Method.GET, Method.POST}, url = "/hello", matching = Matching.EXACT)
public class HelloWorldHandler
{
    HelloWorldServer server;

    @Inject
    HelloWorldHandler() {
        this.server = new HelloWorldServer();
    }


}
