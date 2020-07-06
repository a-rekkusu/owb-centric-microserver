package org.apache.peeco.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class Response
{
    private InputStream output;
    private Map<String, List<String>> headers;

    public Response()
    {
        this.output = new ByteArrayInputStream(new byte[0]);
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public Response(InputStream output)
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

    public InputStream output()
    {
        return output;
    }

    public void setOutput(InputStream stream)
    {
        this.output = stream;
    }

    public void setOutput(String output)
    {
        setOutput(new ByteArrayInputStream(output.getBytes()));
    }
}