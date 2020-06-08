import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class MainApp
{
    private static ContainerLifecycle lifecycle = null;

    public static void main(String[] args) throws Exception
    {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        System.out.println("test");
        shutdown();
    }

    public static void shutdown()
    {
        lifecycle.stopApplication(null);
    }

}
