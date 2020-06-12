package api;

import javax.ws.rs.HttpMethod;

public class Request
{
    public String getMethod()
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

    public String getHeader()
    {
        return "";
    }

}
