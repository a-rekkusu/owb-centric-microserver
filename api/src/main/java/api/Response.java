package api;

import javax.inject.Named;
import java.io.IOException;
import java.io.PrintWriter;

@Named
public class Response extends Servlet
{
    private PrintWriter writer;

    public void setContentType(String contentType)
    {
    }

    public void setCharacterEncoding(String encoding)
    {
    }

    public void setContentLength(int contentLength){

    }

    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }

    public void write(PrintWriter writer)
    {
    }

}
