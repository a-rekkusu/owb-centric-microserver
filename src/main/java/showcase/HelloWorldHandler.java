package showcase;

import api.HttpHandler;
import api.HttpMethodHandler;
import api.Request;
import api.Response;

import static api.Matching.EXACT;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;

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
