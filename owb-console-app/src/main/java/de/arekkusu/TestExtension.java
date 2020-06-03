package de.arekkusu;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import java.util.ArrayList;

public class TestExtension implements Extension
{
    private static ArrayList<String> list = new ArrayList<>();

    <T> void processAnnotatedType(@Observes @WithAnnotations(TestQualifier.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("Process Annotated Type: " + patEvent.getAnnotatedType().getJavaClass().getName());
        list.add(patEvent.getAnnotatedType().getJavaClass().getName());
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) {

    }
}
