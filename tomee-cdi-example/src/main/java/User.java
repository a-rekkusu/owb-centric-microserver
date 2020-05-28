import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class User implements Serializable
{
    private String name = "CDI says: Hello Alex!";

    @Logging
    public String getMyName() {
        return name;
    }

    @PostConstruct
    public void init(){
        System.out.println("Post Construct was called!");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Pre Destroy was called!");
    }
}