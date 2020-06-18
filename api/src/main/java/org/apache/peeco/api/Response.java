package org.apache.peeco.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class Response
{
    private ByteArrayOutputStream outputStream;
    private Map<String, List<String>> headers;

    public Response()
    {
        outputStream = new ByteArrayOutputStream();
        this.headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
    }

    public Response(int outputStreamSize)
    {
        this.outputStream = new ByteArrayOutputStream(outputStreamSize);
        this.headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
    }

    public ByteArrayOutputStream outputStream()
    {
        return outputStream;
    }

    public Map<String, List<String>> headers()
    {
        return headers;
    }

    public ByteArrayInputStream toInputStream(ByteArrayOutputStream outputStream)
    {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public void setOutput(ByteArrayInputStream stream)
    {

    }

    public void setOutput(String output)
    {
        headers().put("content-length", new ArrayList<String>(Arrays.asList(String.valueOf(output.length()))));
        setOutput(new ByteArrayInputStream(output.getBytes()));
    }
}