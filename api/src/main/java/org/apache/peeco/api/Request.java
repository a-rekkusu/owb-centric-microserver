package org.apache.peeco.api;

import java.io.InputStream;
import java.util.HashMap;

public class Request
{
    private HttpMethod httpMethod;
    private String url;
    private InputStream inputStream;
    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;

    public Request(HttpMethod httpMethod, String url, InputStream stream)
    {
        this.httpMethod = httpMethod;
        this.url = url;
        this.inputStream = stream;
        this.headers = new HashMap<String, String>();
        this.parameters = new HashMap<String, String>();
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

    public HashMap<String, String> headers()
    {
        return headers;
    }

    public HashMap<String, String> parameters()
    {
        return parameters;
    }


}