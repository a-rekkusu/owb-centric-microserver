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

    public List<HttpHandlerInfo> collectInfos(Class clazz) throws RuntimeException
    {
        ArrayList<HttpHandlerInfo> infos = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods())
        {
            Class type = method.getDeclaringClass();
            HttpHandler annotation = method.getAnnotation(HttpHandler.class);
            boolean isRequestInParameterTypes = false;

            for (Class parameterType : method.getParameterTypes())
            {
                if (parameterType.equals(Request.class))
                {
                    isRequestInParameterTypes = true;
                }
            }

            if (annotation != null
                    && (method.getReturnType() == Response.class || method.getReturnType() == CompletionStage.class)
                    && isRequestInParameterTypes)
            {
                infos.add(new HttpHandlerInfo(type, method, annotation));
            }
        }

        if (infos.isEmpty())
        {
            throw new RuntimeException("The CDI Container could not find any valid HTTP Handlers. The return type of the annotated method must be " +
                    Response.class.toString() + " or " + CompletionStage.class.toString() + "<Response>. The method signature must contain " +
                    Request.class.toString() + ".");
        }

        return infos;
    }
}
