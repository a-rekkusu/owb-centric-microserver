package org.apache.peeco.impl;

import org.apache.peeco.api.HttpHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandlerExtension implements Extension
{
    private List<HttpHandlerInfo> httpHandlerInfos = new ArrayList<>();
    private HttpServer server;

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");

        List<HttpHandlerInfo> infos = collectInfos(patEvent.getAnnotatedType().getJavaClass());
        for (HttpHandlerInfo info : infos)
        {
            System.out.println("Class: " + info.clazz +
                    ", Method: " + info.method +
                    ", Annotation: " + info.annotation.url() + ", " + Arrays.toString(info.annotation.method()) + ", " + info.annotation.matching());
        }
        
        
        // TODO add validation
        // only 1 method param == Request
        // return value must be REsponse or CompletionStage<Response>
        
        httpHandlerInfos.addAll(infos);
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) throws Exception
    {
        //netty starten, http handler holen, url bei netty registrieren, handler reinhängen
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        server = new HttpServer(httpHandlerInfos);
        server.bootstrap();
    }

    public List<HttpHandlerInfo> collectInfos(Class clazz)
    {
        ArrayList<HttpHandlerInfo> infos = new ArrayList<>();
        
        for (Method method : clazz.getDeclaredMethods())
        {
            Class type = method.getDeclaringClass();
            HttpHandler annotation = method.getAnnotation(HttpHandler.class);
            if (annotation != null)
            {
                infos.add(new HttpHandlerInfo(type, method, annotation));
            }
        }

        return infos;
    }
}
