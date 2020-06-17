package org.apache.peeco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class HelloWorldHandler
{
    private String responseContent = "Hello World from " + getClass().getName();
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


    @HttpHandler(method = {HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
    public CompletionStage<Response> apply(Request request)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response(outputStream);
            response.headers().put("statusCode", "200");

            try
            {
                response.outputStream().write(responseContent.getBytes(Charset.forName("UTF-8")));
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
        Response response = new Response(outputStream);
        response.headers().put("statusCode", "200");
        response.outputStream().write(responseContent.getBytes(Charset.forName("UTF-8")));
        return response;
    }
}
