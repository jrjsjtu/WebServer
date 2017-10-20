package containers;

import Servlet.DefaultServlet;
import loader.WebappClassLoader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarFile;

/**
 * Created by jrj on 17-10-18.
 */
public class UrlServletMap {
    static String resourcePath;
    static {
        resourcePath = System.getProperty("user.dir")+"/webapps/u928/web/WEB-INF/classes";
    }
    ClassLoader webappClassLoader;
    HashMap<String,HttpServlet> internalMap;
    DefaultServlet defaultServlet;
    UrlServletMap(ClassLoader webappClassLoader){
        this.webappClassLoader = webappClassLoader;
        defaultServlet = new DefaultServlet();
        internalMap = new HashMap<>();
        File[] files = new File(resourcePath).listFiles();
        for (File f : files) {
            String fileName = f.getName();
            if (f.isFile() && f.getName().endsWith(".class"))
                try {
                    Class clazz = Class.forName(fileName.substring(0,fileName.lastIndexOf('.')),true,webappClassLoader);
                    insertServletIntoMap(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            System.out.println(f.getName());
        }
        /*
        try {
            Class.forName("Login",false,webappClassLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
    }

    public HttpServlet getServlet(String url){
        if (internalMap.get(url) == null){
            return defaultServlet;
        }else{
            return internalMap.get(url);
        }
    }

    private void insertServletIntoMap(Class clazz){
        if (clazz.isAnnotationPresent(WebServlet.class)){
            WebServlet annotation = (WebServlet) clazz.getAnnotation(WebServlet.class);
            String[] patternList = annotation.urlPatterns();
            for (String pattern:patternList){
                try {
                    internalMap.put(pattern,(HttpServlet) clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        UrlServletMap urlServletMap = new UrlServletMap(new WebappClassLoader());
    }
}
