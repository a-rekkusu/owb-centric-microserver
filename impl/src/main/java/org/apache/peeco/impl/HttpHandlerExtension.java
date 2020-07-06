package org.apache.peeco.impl;

import org.apache.peeco.api.HttpHandler;
import org.apache.peeco.api.Request;
import org.apache.peeco.api.Response;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class HttpHandlerExtension implements Extension
{
    private List<HttpHandlerInfo> httpHandlerInfos = new ArrayList<>();
    private HttpServer server;

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent) throws Exception
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");

        List<HttpHandlerInfo> infos = collectInfos(patEvent.getAnnotatedType().getJavaClass());

        for (HttpHandlerInfo info : infos)
        {
            System.out.println("Class: " + info.clazz +
                    ", Method: " + info.method +
                    ", Annotation: " + info.annotation.url() + ", " + Arrays.toString(info.annotation.method()) + ", " + info.annotation.matching());
        }

        httpHandlerInfos.addAll(infos);
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) throws Exception
    {
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        server = new HttpServer(httpHandlerInfos);
        server.bootstrap();
    }

    public List<HttpHandlerInfo> collectInfos(Class clazz) throws Exception
    {
        ArrayList<HttpHandlerInfo> infos = new ArrayList<>();

        try
        {
            for (Method method : clazz.getDeclaredMethods())
            {
                Class type = method.getDeclaringClass();
                HttpHandler annotation = method.getAnnotation(HttpHandler.class);

                if (annotation != null
                        && (method.getReturnType() == Response.class || method.getReturnType() == CompletionStage.class)
                        && method.getParameterTypes()[0] == Request.class)
                {
                    infos.add(new HttpHandlerInfo(type, method, annotation));
                }
            }
        }
        catch  (Exception ex){
            throw new Exception("The CDI Container could not find any valid HTTP Handlers. The return type of the annotated method must be " +
                    Response.class.toString() + " or "  + CompletionStage.class.toString() + "<Response>. The first argument in the method signature must be " +
                    Request.class.toString() + ".");
        }

        return infos;
    }
}
