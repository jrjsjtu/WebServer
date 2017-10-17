package loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by jrj on 17-10-17.
 */
public class LoaderUtil {
    static void initClassMap(HashMap classMap,String path){
        File[] files = new File(path).listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".jar"))
                try {
                    JarFile jar = new JarFile(f);
                    //ZipEntry zipEntry = jar.getEntry("javax/servlet/http/HttpUpgradeHandler.class");
                    readJAR(jar,classMap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            System.out.println(f.getName());
        }
    }

    static private void readJAR(JarFile jar,HashMap hashMap) throws IOException{
        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()){
            JarEntry je = en.nextElement();
            String name = je.getName();
            if (name.endsWith(".class")){
                hashMap.put(name,jar);
            }
        }
    }

    static byte[] GetByteArray(JarFile jar,String path){
        ZipEntry zipEntry = jar.getEntry(ConvertURL(path));
        try {
            int size = (int)zipEntry.getSize();
            byte[] result = new byte[size];
            InputStream inputStream = jar.getInputStream(zipEntry);
            int curPosition = 0;
            while (curPosition!=size){
                curPosition += inputStream.read(result,curPosition,size-curPosition);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String ConvertURL(String name){
        String pathName = name.replace('.','/');
        return pathName.endsWith(".class")?pathName:(pathName+".class");
    }
}
