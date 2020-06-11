package handlers;

import api.HttpHandler;
import api.Request;
import api.Response;
import api.Servlet;
import api.Matching;
import api.Method;

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
