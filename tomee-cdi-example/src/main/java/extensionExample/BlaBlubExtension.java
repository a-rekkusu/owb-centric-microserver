package extensionExample;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

public class BlaBlubExtension implements Extension
{

    <T> void processAnnotatedType(@Observes @WithAnnotations(BlaBlubQualifier.class) ProcessAnnotatedType<T> patEvent)
    {
        System.out.println("scanning type: " + patEvent.getAnnotatedType().getJavaClass().getName());
    }

}
