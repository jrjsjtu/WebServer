package containers;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by jrj on 17-9-22.
 */
public interface Context {
    void invoke(ServletRequest request, ServletResponse resposne);
}
