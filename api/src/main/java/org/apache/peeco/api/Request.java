package org.apache.peeco.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Request
{
    public HttpMethod httpMethod()
    {
        return HttpMethod.GET;
    }

    public HashMap<String, String> headers(){
        return new HashMap<String, String>();
    }

    public HashMap<String, String> parameters(){
        return new HashMap<String, String>();
    }

    public String url(){
        return "";
    }

    public boolean urlMatchingFound(String url)
    {
        return true;
    }

    public InputStream getPayload(){
        return new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return 0;
            }
        };
    }
}