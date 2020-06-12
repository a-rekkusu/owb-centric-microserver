package api;

import com.sun.mail.iap.ByteArray;

import java.io.IOException;
import java.io.PrintWriter;

public class Response
{
    private PrintWriter writer;

    public void setContentType(String contentType)
    {

    }

    public void setCharacterEncoding(String encoding)
    {

    }

    public void setContentLength(int contentLength)
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

    public void write(ByteArray byteArray)
    {

    }

}
