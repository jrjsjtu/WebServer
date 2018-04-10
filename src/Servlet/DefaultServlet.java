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
        resourcePath = System.getProperty("user.dir")+"/webapps/u928/web";
    }
    private static final Calendar cal = Calendar.getInstance();
    private static final SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    /*
    private static final char[] Ok200String = "HTTP/1.1 200 OK\r\n".toCharArray();
    private static final char[] contentHTML= "Content-Type: text/html; charset=utf-8\r\n".toCharArray();
    private static final char[] contentCSS = "Content-Type: text/css; charset=utf-8\r\n".toCharArray();
    private static final char[] contentJS= "Content-Type: application/javascript\r\n".toCharArray();
    private static final char[] contentJSON = "Content-Type: application/json; charset=utf-8\r\n".toCharArray();
    private static final char[] keepAlive = "Connection: keep-alive\r\n".toCharArray();
    private static final char[] endOfHeader = "\r\n".toCharArray();
    private static final char[] date = "Date: ".toCharArray();
    private static final char[] ContentLength = "Content-Length:".toCharArray();
    */
    private static final String contentHTML= "text/html; charset=utf-8\r\n";
    private static final String contentCSS = "text/css; charset=utf-8\r\n";
    private static final String contentJS= "application/javascript\r\n";
    private static final String contentJSON = "application/json; charset=utf-8\r\n";
    private static final String keepAlive = "keep-alive\r\n";
    private static final String endOfHeader = "\r\n";
    private static final String date = "Date: ";
    private static final String ContentLength = "Content-Length:";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file,fileWithView;
        String resourceName = req.getRequestURI();
        file = new File(resourcePath + resourceName);
        //这里为了迎合hexo,其实做了很多,把所有默认路径映射到了/web/views路径下
        //而且发现如果是目录,就默认导入到目录下的index.html文件上
        fileWithView = new File(resourcePath + "/views"+ resourceName);
        resp.setStatus(200);
        if (file.exists() || (file=fileWithView).exists() ) {
            if(file.isDirectory()){
                file = new File(resourcePath + "/views"+ resourceName + "/index.html");
            }
            long size = file.length();
            switch (file.getName().charAt(file.getName().length() - 1)) {
                case 'm':
                    resp.setHeader("Content-Type: ",contentHTML);
                    //printWriter.write(contentHTML);
                    break;
                case 's':
                    resp.setHeader("Content-Type: ",contentCSS);
                    //printWriter.write(contentCSS);
                    break;
                case 'j':
                    resp.setHeader("Content-Type: ",contentJS);
                    //printWriter.write(contentJS);
                    break;
                default:
                    resp.setHeader("Content-Type: ",contentHTML);
                    //printWriter.write(contentHTML);
                    break;
            }
            //printWriter.write(keepAlive);
            //printWriter.write(date);
            resp.setHeader(date,greenwichDate.format(cal.getTime())+endOfHeader);
            //printWriter.write(greenwichDate.format(cal.getTime()).toCharArray());
            //printWriter.write(endOfHeader);
            resp.setHeader(ContentLength,size+"\r\n");
            resp.setHeader("Powered-by: ","JRJ\r\n");
            //printWriter.write(ContentLength);
            //printWriter.write((size + "").toCharArray());
            //printWriter.write(endOfHeader);
            //printWriter.write(endOfHeader);
            //printWriter.flush();
            if (resp instanceof NioServletResponse){
                ((NioServletResponse)resp).addFileToFlush(file);
            }
        }
    }
}
