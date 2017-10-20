package pipeline;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by jrj on 17-10-20.
 */
public class StandardFilterChain implements FilterChain {
    Filter[] filters;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

    }
}
