import java.util.function.Function;

@HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
public class HelloWorldHandlerInstant implements Function<Request, Response>
{
    @Override
    public Response apply(Request request)
    {
        Response response = new Response();
        response.setStatus(200);
        response.write("Hello World from " + getClass().getName());
        return response;
    }
}