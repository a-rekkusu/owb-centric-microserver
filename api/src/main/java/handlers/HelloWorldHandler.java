package handlers;

import bindings.HttpHandler;
import bindings.Request;
import bindings.Response;
import bindings.Servlet;
import enums.Matching;
import enums.Method;

import javax.inject.Named;

@Named
@HttpHandler(method = {Method.GET, Method.POST}, url = "/hello", matching = Matching.EXACT)
public class HelloWorldHandler extends Servlet
{
    public HelloWorldHandler() {
    }

    @Override
    public void doGet(Request request, Response response)
    {
        super.doGet(request, response);
    }
}
