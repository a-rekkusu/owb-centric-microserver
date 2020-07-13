package org.apache.peeco.showcase;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Bootstrapper
{
    public static void main(String[] args) throws Exception
    {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        try (SeContainer container = initializer.initialize())
        {
            
        }
    }
}
