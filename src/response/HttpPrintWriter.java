package response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by jrj on 17-10-13.
 */
public class HttpPrintWriter extends PrintWriter {
    OutputStream out;
    public HttpPrintWriter(OutputStream out) {
        super(out);
        this.out=  out;
    }

    @Override
    public void write(String str){
        try {
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(char[] str){

    }
}
