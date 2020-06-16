package api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class Response extends Header
{
    private PrintWriter writer;

    public void setPayload(InputStream stream){

    }

    //TOTO setPayload(ReactiveStream)

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