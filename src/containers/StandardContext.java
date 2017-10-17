package containers;

import response.NioServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
    String resourcePath;
    public StandardContext(){
        resourcePath = System.getProperty("user.dir")+"/web/";
    }
    @Override
    public void invoke(ServletRequest request, ServletResponse response) {
    }
}
