import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;


@Interceptor
@Logging
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingBean implements Serializable
{
    @AroundInvoke
    public Object log(InvocationContext invocationContext) throws Exception
    {
        System.out.println("Method " + invocationContext.getMethod().getName() + " was invoked!");
        return invocationContext.proceed();
    }
}
