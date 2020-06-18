package org.apache.peeco.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Response
{
    private ByteArrayOutputStream outputStream;
    private HashMap<String, String> headers;

    public Response(ByteArrayOutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.headers = new HashMap<String, String>();
    }

    public ByteArrayOutputStream outputStream()
    {
        return outputStream;
    }

    public HashMap<String, String> headers()
    {
        return headers;
    }

    public ByteArrayInputStream toInputStream(ByteArrayOutputStream outputStream)
    {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}