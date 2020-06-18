package org.apache.peeco.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Request
{
    private HttpMethod httpMethod;
    private String url;
    private InputStream inputStream;
    private Map<String, List<String>> headers;
    private Map<String, List<String>> parameters;
    private Map<String, List<String>> bodyParameters;
    private Map<String, List<String>> queryParameters;

    public Request(HttpMethod httpMethod, String url, InputStream stream)
    {
        this.httpMethod = httpMethod;
        this.url = url;
        this.inputStream = stream;
        this.headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        this.parameters = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        this.bodyParameters = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        this.queryParameters = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
    }

    public HttpMethod httpMethod()
    {
        return httpMethod;
    }

    public String url()
    {
        return url;
    }

    public InputStream inputStream()
    {
        return inputStream;
    }

    public Map<String, List<String>> headers()
    {
        return headers;
    }

    public Map<String, List<String>> parameters()
    {
        bodyParameters().forEach((s, strings) -> parameters.put(s, strings));
        queryParameters().forEach((s, strings) -> parameters.put(s, strings));
        return parameters;
    }

    public Map<String, List<String>> bodyParameters()
    {
        return bodyParameters;
    }

    public Map<String, List<String>> queryParameters()
    {
        return queryParameters;
    }
}