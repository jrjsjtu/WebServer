package response;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by jrj on 17-10-13.
 */
public class HttpPrintWriter extends PrintWriter {
    public HttpPrintWriter(@NotNull Writer out) {
        super(out);
    }
}
