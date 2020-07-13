package org.apache.peeco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;

import javax.enterprise.context.RequestScoped;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

@RequestScoped
public class HelloWorldHandler
{
    @Inject private HttpServer httpServer;

    @HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/hello*", matching = Matching.WILDCARD)
    public CompletionStage<Response> apply(Request request)
    {
        String responseContent = "Hello World with CompletionStage from " + getClass().getName()
                + " on " + httpServer.getHost() + " and port " + httpServer.getPort();
        ByteArrayInputStream output = new ByteArrayInputStream(responseContent.getBytes(StandardCharsets.UTF_8));

        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response();
            response.addHeader("statusCode", "200");
            response.addHeader("content-type", "text/html");
            response.setOutput(output);
            return response;
        });
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello2", matching = Matching.EXACT)
    public Response applyWithoutCompletionStage(Request request)
    {
        String responseContent = "Hello World from " + getClass().getName();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello3/*", matching = Matching.WILDCARD)
    public Response applyWithWildcardMatching(Request request){
        String responseContent = "Hello with Wildcard URL from " + getClass().getName();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }

    //test - method must not be discovered by processAnnotatedType
    @RequestScoped
    public void noHandlerAnnotation()
    {

    }
}
