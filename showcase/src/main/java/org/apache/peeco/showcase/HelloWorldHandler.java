package org.apache.peeco.showcase;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Matching;
import org.apache.peeco.api.*;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.junit.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class HelloWorldHandler
{
    private static ContainerLifecycle lifecycle = null;

    @HttpHandler(method = {HttpMethod.POST}, url = "/hello", matching = Matching.EXACT)
    public CompletionStage<Response> apply(Request request)
    {
        String responseContent = "Hello World from " + getClass().getName();
        ArrayList<String> statusCodeValues = new ArrayList<>(Arrays.asList("200"));
        ByteArrayInputStream output = new ByteArrayInputStream(responseContent.getBytes(StandardCharsets.UTF_8));

        return CompletableFuture.supplyAsync(() ->
        {
            Response response = new Response();
            response.headers().put("statusCode", statusCodeValues);
            response.setOutput(output);
            //OR as setOutput(String):
            //response.setOutput(responseContent);

            return response;
        });
    }

    @HttpHandler(method = {HttpMethod.GET}, url = "/hello2", matching = Matching.EXACT)
    public Response applyWithoutCompletionStage(Request request)
    {
        String responseContent = "Hello World from " + getClass().getName();
        ArrayList<String> statusCodeValues = new ArrayList<>(Arrays.asList("200"));
        ByteArrayInputStream output = new ByteArrayInputStream(responseContent.getBytes(StandardCharsets.UTF_8));

        Response response = new Response(output);
        response.headers().put("statusCode", statusCodeValues);

        return response;
    }

    //test - diese methode darf von processAnnotatedType nicht berücksichtigt werden
    @RequestScoped
    public void noHandlerAnnotation()
    {

    }

    @Test
    public void start()
    {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
        lifecycle.stopApplication(null);
    }
}
