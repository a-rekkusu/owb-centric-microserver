import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ApplicationScoped
@WebServlet("/hello")
public class SimpleServlet extends HttpServlet
{
    @Inject
    private User user;

    @Inject
    private TestBean testBean;

    public void init() throws ServletException
    {
        //do nothing
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        testBean.getTestString();
        out.println("<h1>" + user.getMyName() + "</h1>");
    }

    public void destroy()
    {
        // do nothing.
    }

}