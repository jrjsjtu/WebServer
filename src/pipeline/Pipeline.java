package pipeline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jrj on 17-10-13.
 */
public interface Pipeline {
    Valve getBasic();
    void setBasic(Valve valve);
    void addBasic(Valve valve);
    Valve[] getValves();
    void invoke(HttpServletRequest request, HttpServletResponse response);
    void removeValve(Valve valve);
}
