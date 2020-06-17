package org.apache.peeco.api;

import java.io.InputStream;
import java.util.HashMap;

public class Request
{
    private InputStream inputStream;
    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;

    public Request(InputStream stream)
    {
        this.inputStream = stream;
        this.headers = new HashMap<String, String>();
        this.parameters = new HashMap<String, String>();
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

    public HttpMethod httpMethod()
    {
        return HttpMethod.GET;
    }

    public String url()
    {
        return "";
    }

    public boolean urlMatchingFound(String url)
    {
        return true;
    }

}