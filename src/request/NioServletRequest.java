package request;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.*;

/**
 * Created by jrj on 17-10-11.
 */
public class NioServletRequest implements HttpServletRequest {
    private int contentLength;
    //status 1.解析url 2.解析header 3.解析content
    private static final int FINISH = 100;
    private static final int CONTENTNOTFINISH = 75;
    private static final int PARSEURL = 25;
    private static final int PARSEHEADER = 50;
    private int status;
    ByteBuffer byteBuffer;
    HashMap<String,String[]> parameterMap;
    Cookie[] cookies;
    String requestURI;
    String method;
    public NioServletRequest(){
        //一开始分配512B，到了后来再根据contentLength来分配
        status = PARSEURL;
        byteBuffer = ByteBuffer.allocate(512);
        parameterMap = new HashMap<>();
    }

    public boolean isFinished(){
        process(byteBuffer.array(),byteBuffer.position());
        if (status == FINISH)
            return true;
        else
            return false;
    }

    private void process(byte[] arrays,int length){
        if (status == CONTENTNOTFINISH){
            if (!byteBuffer.hasRemaining()){
                status = FINISH;
            }
            return;
        }
        int start = 0;
        int curPosition;
        for (curPosition=0;curPosition<length;curPosition++){
            if (arrays[curPosition]!='\n'){
                continue;
            }else{
                if (status == PARSEURL){
                    int spaceNum = 0;int i= start;
                    for (;i<curPosition-1;i++){
                        if (arrays[i] == ' '){
                            spaceNum ++;
                            if(spaceNum ==1){
                                method = new String(arrays,start,i);
                            }else if(spaceNum ==2){
                                requestURI = new String(arrays,start,i-start);
                                break;
                            }
                            start = i+1;
                        }
                    }
                    start = curPosition +1;
                    status = PARSEHEADER;
                }else if(status == PARSEHEADER){
                    if (arrays[start]=='\r'){
                        if (parameterMap.get("Content-Length")==null){
                            status = FINISH;
                            return;
                        }else{
                            status = CONTENTNOTFINISH;
                            int size = Integer.parseInt(parameterMap.get("Content-Length")[0]);
                            byteBuffer = ByteBuffer.allocate(size);
                        }
                        break;
                    }
                    int i=start;
                    for (;i<curPosition-1;i++){
                        if (arrays[i] == ':') break;
                    }
                    String key = new String(arrays,start,i-start);
                    String value = new String(arrays,i+1,curPosition-i-2);
                    parameterMap.put(key,new String[]{value});
                    start = curPosition + 1;
                }
            }
        }
        for (int i=0;i<length - start;i++){
            arrays[i] = arrays[i+start];
        }
        byteBuffer = ByteBuffer.wrap(arrays);
        byteBuffer.position(length-start);
    }

    public static void main(String[] args){
        NioServletRequest nioServletRequest = new NioServletRequest();
        String headers;
        headers = "GET /socket.io/1/xhr-polling/OEUkq93KQLXbgr38t1Wl?t=1507697346439 HTTP/1.1\r\nHost: s8-im-notify.csdn.net\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Content-Length:50\r\n" +
                "\r";
        nioServletRequest.process(headers.getBytes(),headers.length());
    }

    public ByteBuffer getBuffer(){
        return byteBuffer;
    }
    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }
}
