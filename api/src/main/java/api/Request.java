package api;

import java.io.IOException;
import java.io.InputStream;

public class Request extends Header
{
    public HttpMethod getMethod()
    {
        return HttpMethod.GET;
    }

    public boolean urlMatchingFound(String url)
    {
        return true;
    }

    public String getParameter(String parameter)
    {
        return "";
    }

    public InputStream getPayload(){
        return new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return 0;
            }
        };
    }
}