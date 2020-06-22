package org.apache.peeco.impl;

import org.apache.peeco.api.HttpHandler;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.junit.Test;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;

public class HttpHandlerExtension implements Extension
{
    private static ArrayList<String> list = new ArrayList<>();

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");
        System.out.println("Process Annotated Type Classes with HttpHandler: " + patEvent.getAnnotatedType().getJavaClass().getName());
        System.out.println("Process Annotated Type Methods with HttpHandler: " + patEvent.getAnnotatedType().getMethods().toString());
        list.add(patEvent.getAnnotatedType().getMethods().toString());
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv)
    {

        //netty starten, http handler holen, url bei netty registrieren, handler reinhängen
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        for (String s : list)
        {
            System.out.println("After Deployment Validation: " + s);
        }
    }
}
