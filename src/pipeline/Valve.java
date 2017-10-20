package pipeline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jrj on 17-10-13.
 */
public interface Valve {
    void invoke(HttpServletRequest request, HttpServletResponse response);
    String getInfo();
}
