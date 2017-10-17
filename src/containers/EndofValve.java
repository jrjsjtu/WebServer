package containers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jrj on 17-10-13.
 */
public class EndofValve implements Valve {
    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public String getInfo() {
        return null;
    }
}
