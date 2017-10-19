package loader;

import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by jrj on 17-10-17.
 */
public class StandardLoader extends ClassLoader {
    //这个loader从lib读取jar
    static String resourcePath;
    static HashMap<String,JarFile> classMap;
    static {
        resourcePath = System.getProperty("user.dir")+"/lib/";
        classMap = new HashMap<>();
        LoaderUtil.initJarMap(classMap,resourcePath);
    }
    StandardLoader(ClassLoader parent){
        super(parent);
    }
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        String normalPath = LoaderUtil.ConvertURL(name);
        if (classMap.get(normalPath)!=null){
            byte[] bytes = LoaderUtil.GetByteArray(classMap.get(normalPath),name);
            Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
            return c;
        }
        return super.findClass(normalPath);
    }

    public void Test(){

    }
}
