package de.arekkusu;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

public class MainApp
{
    private static ContainerLifecycle lifecycle = null;

    public static void main(String[] args) throws Exception
    {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        BeanManager beanManager = lifecycle.getBeanManager();
        Bean<?> bean = beanManager.getBeans("qualifierBean1").iterator().next();
        QualifierBean1 qualifierBean1 = (QualifierBean1) lifecycle.getBeanManager().getReference(bean, QualifierBean1.class, beanManager.createCreationalContext(bean));

        qualifierBean1.writeToConsole();

        System.out.println("test");
        shutdown();
    }

    public static void shutdown()
    {
        lifecycle.stopApplication(null);
    }

}
