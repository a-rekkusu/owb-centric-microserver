package api;

import javax.inject.Named;

@Named
public class Servlet
{

    public void doGet(Request request, Response response){
        System.out.println("Hello world");
    }

    public void doPost(Request request, Response response){

    }
}
