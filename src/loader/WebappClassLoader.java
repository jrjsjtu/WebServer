package loader;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.util.HashMap;
import java.util.jar.JarFile;

/**
 * Created by jrj on 17-10-17.
 */
public class WebappClassLoader extends ClassLoader {
    static String resourcePath;
    static HashMap<String,JarFile> classMap;
    static {
        resourcePath = System.getProperty("user.dir")+"/webapps/u928/web/WEB-INF/classes/";
    }
    public WebappClassLoader(){
        super(new StandardLoader(ClassLoader.getSystemClassLoader()));
    }
    public WebappClassLoader(ClassLoader parent){
        super(parent);
    }

    //载入的时候应该要用权限定名 比如xx.xxx.xxx.xx.login而这后面是不要跟.class的
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        String normalPath = LoaderUtil.ConvertURLForClass(name);
        String absolutePath = resourcePath + normalPath;
        File file = new File(absolutePath);
        if (file.exists()){
            byte[] bytes = LoaderUtil.GetByteArray(file);
            Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
            return c;
        }
        return super.findClass(normalPath);
    }

    public static void main(String[] args) throws Exception {
        //这里直接把父载入器设置为ext,就可以跳过app载入器了!
        StandardLoader mcl = new StandardLoader(ClassLoader.getSystemClassLoader());
        WebappClassLoader webappClassLoader = new WebappClassLoader(mcl);
        Class<?> c1 = Class.forName("Login", true, webappClassLoader);
        Object obj = c1.newInstance();
    }
}
