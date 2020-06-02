import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestBean
{
    private String test = "Request string!";

    @Logging
    public String getTestString(){
        return test;
    }

    @PostConstruct
    public void init(){
        System.out.println("Test Post Construct was called!");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Test Pre Destroy was called!");
    }
}