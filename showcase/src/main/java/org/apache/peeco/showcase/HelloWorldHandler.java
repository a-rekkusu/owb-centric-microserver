package org.apache.peeco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class HelloWorldHandler
{
    @HttpHandler(method = {HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
    public CompletionStage<Response> apply(Request request)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String responseContent = "Hello World from " + getClass().getName();

        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response(outputStream);
            response.headers().put("statusCode", "200");

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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String responseContent = "Hello World from " + getClass().getName();

        Response response = new Response(outputStream);
        response.headers().put("statusCode", "200");
        response.outputStream().write(responseContent.getBytes(StandardCharsets.UTF_8));
        return response;
    }
}
