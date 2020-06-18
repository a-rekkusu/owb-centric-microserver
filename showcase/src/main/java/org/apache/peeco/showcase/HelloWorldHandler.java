package org.apache.peeco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class HelloWorldHandler
{
    @HttpHandler(method = {HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
    public CompletionStage<Response> apply(Request request)
    {
        String responseContent = "Hello World from " + getClass().getName();
        ArrayList<String> statusCodeValues = new  ArrayList<String>(Arrays.asList("200"));

        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response();
            response.headers().put("statusCode", statusCodeValues);

            try
            {
                response.outputStream().write(responseContent.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return response;
        });
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello", matching = Matching.EXACT)
    public Response applyWithoutCompletionStage(Request request) throws IOException
    {
        String responseContent = "Hello World from " + getClass().getName();
        ArrayList<String> statusCodeValues = new  ArrayList<String>(Arrays.asList("200"));

        Response response = new Response(50);
        response.headers().put("statusCode", statusCodeValues);
        response.outputStream().write(responseContent.getBytes(StandardCharsets.UTF_8));
        return response;
    }
}
