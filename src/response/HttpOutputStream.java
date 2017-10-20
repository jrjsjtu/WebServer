package response;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by jrj on 17-10-13.
 */
public class HttpOutputStream extends ServletOutputStream {
    ByteBuffer byteBufferForContent;
    ByteBuffer byteBufferForHeader;
    SocketChannel socketChannel;
    private int contentLength = 0;
    HttpOutputStream(SocketChannel socketChannel){
        this.byteBufferForHeader = ByteBuffer.allocate(512);
        this.socketChannel = socketChannel;
    }

    public void writeHeader(String str){
        byteBufferForHeader.put(str.getBytes());
    }

    public int getLength(){
        return contentLength;
    }
    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if(byteBufferForContent == null){
            byteBufferForContent = ByteBuffer.allocate(512);
        }
        contentLength += len;
        byteBufferForContent.put(b,off,len);
    }

    @Override
    public void write(byte b[]) throws IOException {
        if(byteBufferForContent == null){
            byteBufferForContent = ByteBuffer.allocate(512);
        }
        contentLength += b.length;
        byteBufferForContent.put(b);
    }

    @Override
    public void flush() throws IOException {
        byteBufferForHeader.flip();
        socketChannel.write(byteBufferForHeader);
        if (byteBufferForContent != null){
            byteBufferForContent.flip();
            socketChannel.write(byteBufferForContent);
        }
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
