package response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by jrj on 17-10-13.
 */
public class HttpOutputStream extends OutputStream {
    ByteBuffer byteBuffer;
    SocketChannel socketChannel;
    HttpOutputStream(SocketChannel socketChannel){
        this.byteBuffer = ByteBuffer.allocate(512);
        this.socketChannel = socketChannel;
    }
    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        byteBuffer.put(b,off,len);
    }

    @Override
    public void write(byte b[]) throws IOException {
        byteBuffer.put(b);
    }

    @Override
    public void flush() throws IOException {
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}
