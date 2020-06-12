package showcase;

import api.*;

import static api.HttpMethod.POST;
import static api.Matching.EXACT;
import static api.HttpMethod.GET;


@HttpHandler(method = {GET, POST}, url = "/hello", matching = EXACT)
public class HelloWorldHandler extends HttpMethodHandler
{
    public HelloWorldHandler() {
    }

    public void helloWorld(Request req, Response resp)
    {
        handleHttpMethod(req.getMethod(), req, resp);
    }
}
