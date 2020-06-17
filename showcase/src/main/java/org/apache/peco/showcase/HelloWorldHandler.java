package org.apache.peco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class HelloWorldHandler
{
    @HttpHandler(method = {HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
    public CompletionStage<Response> apply(Request request)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response();
            response.headers().put("statusCode", "200");
            response.write("Hello World from " + getClass().getName());
            return response;
        });
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello", matching = Matching.EXACT)
    public Response applyWithoutCompletionStage(Request request)
    {
        Response response = new Response();
        response.headers().put("statusCode", "200");
        response.write("Hello World from " + getClass().getName());
        return response;
    }
}
