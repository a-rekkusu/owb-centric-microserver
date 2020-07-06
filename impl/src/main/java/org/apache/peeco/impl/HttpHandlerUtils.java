package org.apache.peeco.impl;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.peeco.api.Matching;

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

    public static HttpHandlerInfo getMatchingHandler(HttpRequest nettyRequest, List<HttpHandlerInfo> infos) throws RuntimeException
    {
        List<HttpHandlerInfo> matchings = new ArrayList<>();

        for (HttpHandlerInfo info : infos)
        {
            if (info.annotation.matching().equals(Matching.EXACT))
            {
                if (nettyRequest.uri().equals(info.annotation.url())
                        && Arrays.asList(info.annotation.method()).contains(mapHttpMethod(nettyRequest.method())))
                {
                    matchings.add(info);
                }
            }
            else if (info.annotation.matching().equals(Matching.WILDCARD))
            {
                String uri = info.annotation.url();
                String url = uri.substring(0, uri.length() - 2);

                if (uri.endsWith("/*") && nettyRequest.uri().startsWith(url)
                        && Arrays.asList(info.annotation.method()).contains(mapHttpMethod(nettyRequest.method())))
                {
                    matchings.add(info);
                }
            }
        }

        if (matchings.size() > 1)
        {
            throw new RuntimeException("Multiple HttpHandlers were found for the incoming Netty Request URI: " + nettyRequest.uri() +
                    ". Only one method is allowed.");
        }

        return matchings.isEmpty() ? null : matchings.get(0);
    }
}
