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
public class ContextWrapper implements Context{
    private static final byte[] Ok200String = "HTTP/1.1 200 OK\r\n".getBytes();
    private static final byte[] contentHTML= "Content-Type: text/html; charset=utf-8\r\n".getBytes();
    private static final byte[] contentCSS = "Content-Type: text/css; charset=utf-8\r\n".getBytes();
    private static final byte[] contentJS= "Content-Type: application/javascript\r\n".getBytes();
    private static final byte[] contentJSON = "Content-Type: application/json; charset=utf-8\r\n".getBytes();
    private static final byte[] keepAlive = "Connection: keep-alive\r\n".getBytes();
    private static final byte[] endOfHeader = "\r\n".getBytes();
    private static final byte[] date = "Date: ".getBytes();
    private static final byte[] ContentLenth = "Content-Length:".getBytes();
    private static final Calendar cal = Calendar.getInstance();
    private static final SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    String resourcePath;
    public ContextWrapper(){
        resourcePath = System.getProperty("user.dir")+"/web/";
    }
    @Override
    public void invoke(ServletRequest request, ServletResponse response) {
        ByteBuffer outBytebuffer = ByteBuffer.allocate(512);
        File directory;
        if (request instanceof HttpServletRequest){
            String resourceName = ((HttpServletRequest)request).getRequestURI();
            directory = new File(resourcePath + resourceName);
            if (directory.exists()){
                outBytebuffer.put(Ok200String);
                long size = directory.length();
                switch (resourceName.charAt(resourceName.length()-1)){
                    case 'm':
                        outBytebuffer.put(contentHTML);
                        break;
                    case 's':
                        outBytebuffer.put(contentCSS);
                        break;
                    case 'j':
                        outBytebuffer.put(contentJS);
                        break;
                    default:
                        outBytebuffer.put(contentHTML);
                        break;
                }
                outBytebuffer.put(keepAlive);
                outBytebuffer.put(date);
                outBytebuffer.put(greenwichDate.format(cal.getTime()).getBytes());
                outBytebuffer.put(endOfHeader);
                outBytebuffer.put(ContentLenth);
                outBytebuffer.put((size+"").getBytes());
                outBytebuffer.put(endOfHeader);outBytebuffer.put(endOfHeader);

                //传输文件
                NioServletResponse nioServletResponse =(NioServletResponse)response;
                nioServletResponse.flushBuffer(outBytebuffer);
                nioServletResponse.flushFile(directory);
            }
        }
    }
}
