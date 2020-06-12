package api;

public class Request
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

    public String getHeader()
    {
        return "";
    }
}