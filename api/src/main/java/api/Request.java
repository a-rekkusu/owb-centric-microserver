package api;

import javax.inject.Named;

@Named
public class Request extends Servlet
{
    public String getMethod(){
        return "";
    }

    public boolean urlMatchingFound(String url){
        return true;
    }

    public String getParameter(String parameter){
        return "";
    }

    public String getHeader(){
        return "";
    }

}
