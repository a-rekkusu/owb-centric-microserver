package org.apache.peeco.impl;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpHandlerUtils
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

    public static HttpHandlerInfo getMatchingHandler(HttpRequest request, List<HttpHandlerInfo> infos)
    {
        List<HttpHandlerInfo> matchings = new ArrayList<>();

        for (HttpHandlerInfo info : infos)
        {
            if (request.uri().equals(info.annotation.url())
                    && Arrays.asList(info.annotation.method()).contains(mapHttpMethod(request.method())))
            {
                matchings.add(info);
            }
        }



        // TODO wildcard matching e.g. restfull urls

        if (matchings.size() > 1)
        {
            // TODO
            // throw exception because only one handler is allowed for matching url/method?
        }

        return matchings.isEmpty() ? null : matchings.get(0);
    }
}
