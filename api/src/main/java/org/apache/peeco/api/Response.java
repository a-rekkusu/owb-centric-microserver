package org.apache.peeco.api;

import java.io.OutputStream;
import java.util.HashMap;

public class Response
{
    private OutputStream outputStream;
    private HashMap<String, String> headers;

    public Response(OutputStream stream)
    {
        this.outputStream = stream;
        this.headers = new HashMap<String, String>();
    }

    public OutputStream outputStream()
    {
        return outputStream;
    }

    public HashMap<String, String> headers()
    {
        return headers;
    }
}