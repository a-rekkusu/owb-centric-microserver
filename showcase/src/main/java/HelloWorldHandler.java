@HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
public class HelloWorldHandler extends HttpMethodHandler
{
    public HelloWorldHandler() {
    }

    public void helloWorld(Request req, Response resp)
    {
        handleHttpMethod(req.getMethod(), req, resp);
    }
}