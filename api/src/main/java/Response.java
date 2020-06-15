import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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

    public void write(ByteArrayInputStream byteArray)
    {

    }

    public CompletionStage<Response> onRequest(Request req)
    {
        return CompletableFuture.supplyAsync(() -> new Response());
    }

    public Response onReq(Request req)
    {
        return this;
    }

}