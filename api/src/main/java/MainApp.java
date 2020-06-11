import api.Request;
import api.Response;
import handlers.HelloWorldHandler;
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
        Bean<?> bean = beanManager.getBeans("helloWorldHandler").iterator().next();
        HelloWorldHandler helloWorldHandler = (HelloWorldHandler) lifecycle.getBeanManager().getReference(bean, HelloWorldHandler.class, beanManager.createCreationalContext(bean));

        helloWorldHandler.doGet(new Request(), new Response());

        System.out.println("test");
        shutdown();
    }

    public static void shutdown()
    {
        lifecycle.stopApplication(null);
    }

}
