import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
public class HelloWorldHandlerFuture implements Function<Request, CompletionStage<Response>>
{
    @Override
    public CompletionStage<Response> apply(Request request)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response();
            response.setStatus(200);
            response.write("Hello World from " + getClass().getName());
            return response;
        });
    }
}