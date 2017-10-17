package Servlet;

import request.NioServletRequest;
import response.NioServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jrj on 17-10-13.
 */
public class DefaultServlet extends HttpServlet {
    static String resourcePath;
    static {
        resourcePath = System.getProperty("user.dir")+"/web/";
    }
    private static final Calendar cal = Calendar.getInstance();
    private static final SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    private static final char[] Ok200String = "HTTP/1.1 200 OK\r\n".toCharArray();
    private static final char[] contentHTML= "Content-Type: text/html; charset=utf-8\r\n".toCharArray();
    private static final char[] contentCSS = "Content-Type: text/css; charset=utf-8\r\n".toCharArray();
    private static final char[] contentJS= "Content-Type: application/javascript\r\n".toCharArray();
    private static final char[] contentJSON = "Content-Type: application/json; charset=utf-8\r\n".toCharArray();
    private static final char[] keepAlive = "Connection: keep-alive\r\n".toCharArray();
    private static final char[] endOfHeader = "\r\n".toCharArray();
    private static final char[] date = "Date: ".toCharArray();
    private static final char[] ContentLength = "Content-Length:".toCharArray();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file;
        String resourceName = req.getRequestURI();
        file = new File(resourcePath + resourceName);
        if (file.exists()) {
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(Ok200String);
            long size = file.length();
            switch (resourceName.charAt(resourceName.length() - 1)) {
                case 'm':
                    printWriter.write(contentHTML);
                    break;
                case 's':
                    printWriter.write(contentCSS);
                    break;
                case 'j':
                    printWriter.write(contentJS);
                    break;
                default:
                    printWriter.write(contentHTML);
                    break;
            }
            printWriter.write(keepAlive);
            printWriter.write(date);
            printWriter.write(greenwichDate.format(cal.getTime()).toCharArray());
            printWriter.write(endOfHeader);
            printWriter.write(ContentLength);
            printWriter.write((size + "").toCharArray());
            printWriter.write(endOfHeader);
            printWriter.write(endOfHeader);
            printWriter.flush();
            if (resp instanceof NioServletResponse){
                ((NioServletResponse)resp).flushFile(file);
            }
        }
    }
}
