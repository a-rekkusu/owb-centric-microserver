package org.apache.peeco.impl;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Method;

import org.apache.peeco.api.Matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Request;
import org.apache.peeco.api.Response;

public class PeecoUtils
{

    public static org.apache.peeco.api.HttpMethod mapHttpMethod(HttpMethod method)
    {
        switch (method.name())
        {
            case "GET":
                return org.apache.peeco.api.HttpMethod.GET;
            case "POST":
                return org.apache.peeco.api.HttpMethod.POST;
            case "PUT":
                return org.apache.peeco.api.HttpMethod.PUT;
            case "DELETE":
                return org.apache.peeco.api.HttpMethod.DELETE;
            case "OPTIONS":
                return org.apache.peeco.api.HttpMethod.OPTIONS;
            case "HEAD":
                return org.apache.peeco.api.HttpMethod.HEAD;
            case "TRACE":
                return org.apache.peeco.api.HttpMethod.TRACE;
            case "CONNECT":
                return org.apache.peeco.api.HttpMethod.CONNECT;
            case "PATCH":
                return org.apache.peeco.api.HttpMethod.PATCH;
        }

        return null;
    }

    public static HttpHandlerInfo getMatchingHandler(HttpRequest nettyRequest, List<HttpHandlerInfo> infos) throws RuntimeException
    {
        List<HttpHandlerInfo> matchings = new ArrayList<>();

        for (HttpHandlerInfo info : infos)
        {
            String incomingUrl = nettyRequest.uri();
            String handlerUrl = info.annotation.url();

            if (isMatching(info.annotation.matching(), handlerUrl, incomingUrl)
                    && Arrays.asList(info.annotation.method()).contains(mapHttpMethod(nettyRequest.method())))
            {
                matchings.add(info);
            }
        }

        if (matchings.size() > 1)
        {
            throw new RuntimeException("Multiple HttpHandlers were found for the incoming Netty Request URI: " + nettyRequest.uri() +
                    ". Only one method is allowed.");
        }

        return matchings.isEmpty() ? null : matchings.get(0);
    }

    public static boolean isMatching(Matching matching, String configuredUrl, String incomingUrl)
    {
        boolean matchingResult = false;
        if (incomingUrl.contains("?"))
        {
            incomingUrl = incomingUrl.substring(0, incomingUrl.indexOf("?"));
        }


        if (matching == Matching.WILDCARD)
        {
            if (configuredUrl.startsWith("*"))
            {
                if (incomingUrl.endsWith(configuredUrl.substring(1)))
                {
                    matchingResult = true;
                }
            }
            else if (configuredUrl.endsWith("*"))
            {
                if (incomingUrl.startsWith(configuredUrl.substring(0, configuredUrl.length() - 1)))
                {
                    matchingResult = true;
                }
            }
        }
        else if (matching == Matching.EXACT)
        {
            if (configuredUrl.equals(incomingUrl))
            {
                matchingResult = true;
            }
        }

        return matchingResult;
    }

    public static List<HttpHandlerInfo> collectInfos(Class clazz) throws RuntimeException
    {
        ArrayList<HttpHandlerInfo> infos = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods())
        {
            if (method.isAnnotationPresent(HttpHandler.class))
            {
                Class type = method.getDeclaringClass();
                HttpHandler annotation = method.getAnnotation(HttpHandler.class);
                boolean isRequestInParameterTypes = false;

                for (Class parameterType : method.getParameterTypes())
                {
                    if (parameterType.equals(Request.class))
                    {
                        isRequestInParameterTypes = true;
                    }
                }

                if (annotation != null
                        && (method.getReturnType() == Response.class || method.getReturnType() == CompletionStage.class)
                        && isRequestInParameterTypes)
                {
                    infos.add(new HttpHandlerInfo(type, method, annotation));
                }
                else
                {
                    throw new RuntimeException("Invalid method signature: " + method + ". The return type of the annotated method must be " +
                            Response.class.toString() + " or " + CompletionStage.class.toString() + "<Response>. The method signature must contain " +
                            Request.class.toString() + ".");
                }
            }
        }

        return infos;
    }

}
