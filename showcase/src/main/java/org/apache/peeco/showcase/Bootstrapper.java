package org.apache.peeco.showcase;

import org.apache.webbeans.service.ClassLoaderProxyService;
import org.apache.webbeans.spi.DefiningClassService;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Bootstrapper
{
    public static void main(String[] args) throws Exception
    {
        try (final SeContainer container = SeContainerInitializer.newInstance()
                .addProperty(DefiningClassService.class.getName(), ClassLoaderProxyService.LoadFirst.class.getName())
                .addProperty("org.apache.webbeans.proxy.useStaticNames", "true")
                .initialize())
        {
            HelloWorldHandler handler = container.select(HelloWorldHandler.class).get();
            System.out.println(handler);
        }
    }
}
