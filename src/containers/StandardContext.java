package containers;

import loader.WebappClassLoader;
import response.NioServletResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jrj on 17-10-12.
 */
public class StandardContext implements Context{
    private static final Calendar cal = Calendar.getInstance();
    private static final SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    private ClassLoader classLoader;
    private UrlServletMap urlServletMap;
    String resourcePath;
    public StandardContext(){
        resourcePath = System.getProperty("user.dir")+"/web/";
        classLoader = new WebappClassLoader();
        urlServletMap = new UrlServletMap(classLoader);
    }
    @Override
    public void invoke(ServletRequest request, ServletResponse response) {
        if (request instanceof HttpServletRequest){
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            try {
                HttpServlet httpServlet = urlServletMap.getServlet(httpServletRequest.getRequestURI());
                httpServlet.service(request,response);
                response.flushBuffer();
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
