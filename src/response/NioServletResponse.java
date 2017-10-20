package response;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by jrj on 17-10-12.
 */
public class NioServletResponse implements HttpServletResponse {
    SocketChannel channel;
    PrintWriter printWriter;
    HttpOutputStream httpOutputStream;
    File fileToResponse;
    StringBuilder stringBuilder;
    private int status;
    private static final String Ok200String = "HTTP/1.1 200 OK\r\n";
    public NioServletResponse(SocketChannel channel){
        this.channel = channel;
        this.httpOutputStream = new HttpOutputStream(channel);
        stringBuilder = new StringBuilder();
        printWriter = new HttpPrintWriter(this.httpOutputStream);
    }

    public void addFileToFlush(File fileToResponse){
        this.fileToResponse = fileToResponse;
    }
    public void flushFile(File file){
        try {
            FileChannel in1 = new FileInputStream(file).getChannel();
            in1.transferTo(0,file.length(),channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {

    }

    @Override
    public void sendError(int i) throws IOException {

    }

    @Override
    public void sendRedirect(String s) throws IOException {

    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {
        stringBuilder.append(s);stringBuilder.append(s1);
    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setIntHeader(String s, int i) {

    }

    @Override
    public void addIntHeader(String s, int i) {

    }

    @Override
    public void setStatus(int i) {
        status = i;
    }

    @Override
    public void setStatus(int i, String s) {

    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return httpOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }
    private static final String ContentLength = "Content-Length:";
    @Override
    public void flushBuffer() throws IOException {
        if(status > -100){
            httpOutputStream.writeHeader(Ok200String);
            httpOutputStream.writeHeader(stringBuilder.toString());
            if (fileToResponse !=null){
                httpOutputStream.writeHeader("\r\n");
                httpOutputStream.flush();
                if (fileToResponse != null){
                    flushFile(fileToResponse);
                }
            }else{
                int length  = httpOutputStream.getLength();
                httpOutputStream.writeHeader(ContentLength+length + "\r\n");
                httpOutputStream.writeHeader("Content-Type: "+"text/html; charset=utf-8\r\n");
                httpOutputStream.writeHeader("\r\n");
                httpOutputStream.flush();
            }
        }
    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
