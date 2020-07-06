package org.apache.peeco.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class Response
{
    private ByteArrayInputStream output;
    private Map<String, List<String>> headers;

    public Response()
    {
        this.output = new ByteArrayInputStream(new byte[0]);
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public Response(ByteArrayInputStream output)
    {
        this.output = output;
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public Map<String, List<String>> headers()
    {
        return headers;
    }

    public void addHeader(String name, String value)
    {
        headers().computeIfAbsent(name, k -> new ArrayList<>())
                .add(value);
    }

    public ByteArrayInputStream output()
    {
        return output;
    }

    public void setOutput(InputStream stream)
    {
        this.output = (ByteArrayInputStream) stream;
    }

    public void setOutput(String output)
    {
        addHeader("content-length", String.valueOf(output.length()));
        setOutput(new ByteArrayInputStream(output.getBytes()));
    }
}