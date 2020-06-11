package api;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;

public class HttpHandlerExtension implements Extension
{
    private static ArrayList<String> list = new ArrayList<String>();

    <T> void processAnnotatedType(@Observes @WithAnnotations(HttpHandler.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("----PROCESS ANNOTATED TYPE----");
        System.out.println("Process Annotated Type: " + patEvent.getAnnotatedType().getJavaClass().getName());
        list.add(patEvent.getAnnotatedType().getJavaClass().getName());
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv)
    {
        System.out.println("----AFTER DEPLOYMENT VALIDATION----");
        for (String s : list)
        {
            System.out.println("After Deployment Validation: " + s);
        }
    }
}
