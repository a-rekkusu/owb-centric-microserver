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

    @HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/hello/*", matching = Matching.WILDCARD)
    public CompletionStage<Response> apply(Request request)
    {
        String responseContent = "Responding to /hello/* : Hello World with CompletionStage from " + getClass().getName()
                + " on " + httpServer.getHost() + " and port " + httpServer.getRuntimePort();
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
        String responseContent = "Responding to /hello2 : Hello World from " + getClass().getName() + "Request query Params: " + request.queryParameters();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello3/*", matching = Matching.WILDCARD)
    public Response applyWithWildcardMatching(Request request){
        String responseContent = "Responding to /hello3/* : Hello with Wildcard URL from " + getClass().getName();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }

    @HttpHandler(method = {HttpMethod.GET, HttpMethod.POST}, url = "/input", matching = Matching.EXACT)
    public Response inputForm(Request request){
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\r\n")
                .append("<html><head><meta charset='utf-8' /><title>")
                .append("Name Input Form")
                .append("</title></head><body>\r\n")
                .append("<form action=\"post\" method=\"POST\">\r\n")
                .append("Enter your name: \r\n")
                .append("<input type=\"text\" name=\"user\" />\r\n")
                .append("<input type=\"submit\" value=\"Submit\" />\r\n")
                .append("</form>\r\n")
                .append("</body></html>\r\n");

        String responseContent = builder.toString();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }

    @HttpHandler(method = {HttpMethod.POST}, url = "/post", matching = Matching.EXACT)
    public Response postRequest(Request request){
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\r\n")
                .append("<html><head><meta charset='utf-8' /><title>")
                .append("Welcome title")
                .append("</title></head><body>\r\n")
                .append("Echo from Netty: welcome " + request.bodyParameters().get("user").get(0) + "!\r\n")
                .append("</body></html>\r\n");

        String responseContent = builder.toString();
        ByteArrayInputStream output = new ByteArrayInputStream(
                responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.addHeader("statusCode", "200");
        response.addHeader("content-type", "text/html");

        return response;
    }
}
