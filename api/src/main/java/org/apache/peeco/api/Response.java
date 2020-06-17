package org.apache.peeco.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class Response
{
    private PrintWriter writer;

    public HashMap<String, String> headers()
    {
        return new HashMap<String, String>();
    }

    public void setPayload(InputStream stream)
    {

    }

    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }

    public void write(PrintWriter writer)
    {

    }

    public void write(String string)
    {

    }
}