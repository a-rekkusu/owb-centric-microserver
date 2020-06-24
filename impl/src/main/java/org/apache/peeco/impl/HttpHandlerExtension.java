package org.apache.peeco.impl;

import org.apache.peeco.api.HttpHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class HttpHandlerExtension implements Extension
{
    private static Map<String, Annotation> annotations = new HashMap<>();
    private HttpServer server;

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");

        getAnnotations(patEvent.getAnnotatedType().getJavaClass());

        for(Map.Entry<String, Annotation> entry : annotations.entrySet()) {
            String key = entry.getKey();
            Annotation value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value.toString());
        }
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) throws Exception
    {
        //netty starten, http handler holen, url bei netty registrieren, handler reinhängen
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        server = new HttpServer();
        server.bootstrap();
    }

    public Map<String, Annotation> getAnnotations(Class clazz)
    {
        for (Method method : clazz.getDeclaredMethods())
        {
            Class type = method.getDeclaringClass();
            String methodName = method.getName();
            Annotation[] allAnnotations = method.getDeclaredAnnotations();

            for (Annotation a : allAnnotations)
            {
                if (a.toString().contains("@org.apache.peeco.api.HttpHandler"))
                {
                    annotations.put(type + "." + methodName, a);
                }
            }
        }

        return annotations;
    }
}
