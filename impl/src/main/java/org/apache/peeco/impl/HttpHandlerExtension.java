package org.apache.peeco.impl;

import org.apache.peeco.api.HttpHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandlerExtension implements Extension
{
    private static List<HttpHandlerInfo> httpHandlerInfo = new ArrayList<>();
    private HttpServer server;

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");

        getAnnotations(patEvent.getAnnotatedType().getJavaClass());

        for(HttpHandlerInfo info : httpHandlerInfo) {
            System.out.println("Class: " + info.clazz + ", Method: " + info.method + ", Annotation: " + info.annotation);
        }
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) throws Exception
    {
        //netty starten, http handler holen, url bei netty registrieren, handler reinhängen
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        server = new HttpServer();
        server.bootstrap();
    }

    public List<HttpHandlerInfo> getAnnotations(Class clazz)
    {
        for (Method method : clazz.getDeclaredMethods())
        {
            Class type = method.getDeclaringClass();
            Annotation[] allAnnotations = method.getDeclaredAnnotations();

            for (Annotation annotation : allAnnotations)
            {
                if (annotation.toString().contains("@org.apache.peeco.api.HttpHandler"))
                {
                    httpHandlerInfo.add(new HttpHandlerInfo(type, method, annotation));
                }
            }
        }
        return httpHandlerInfo;
    }
}
