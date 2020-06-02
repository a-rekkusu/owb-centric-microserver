package extensionExample;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;

public class BlaBlubExtension implements Extension
{
    private static ArrayList<String> list = new ArrayList<String>();

    <T> void processAnnotatedType(@Observes @WithAnnotations(BlaBlubQualifier.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("Process Annotated Type: " + patEvent.getAnnotatedType().getJavaClass().getName());
        list.add(patEvent.getAnnotatedType().getJavaClass().getName());
    }

    void afterTypeDiscovery(@Observes AfterTypeDiscovery atd)
    {
        System.out.println("----AFTER TYPE DISCOVERY----");
        for (String s : list)
        {
            System.out.println("After Type Discovery: " + s);
        }
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd)
    {
        System.out.println("----AFTER BEAN DISCOVERY----");
        for (String s : list)
        {
            System.out.println("After Bean Discovery: " + s);
        }
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
