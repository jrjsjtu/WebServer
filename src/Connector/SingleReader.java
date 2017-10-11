package Connector;

import request.NioServletRequest;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jrj on 17-10-10.
 */
public class SingleReader implements Runnable{
    Selector selector;
    LinkedBlockingQueue<Runnable> linkedBlockingQueue;
    Thread currentThread;
    SingleReader(){
        try {
            selector = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void register(SocketChannel channel){
        try {
            channel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    void execute(Runnable task){

    }


    @Override
    public void run() {
        try{
            while(true){
                int keys = selector.select();
                if (keys>0){
                    Iterator selectorKeys = selector.selectedKeys().iterator();
                    while (selectorKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectorKeys.next();
                        selectorKeys.remove();
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            NioServletRequest servletRequest;
                            if (key.attachment() == null){
                                servletRequest = new NioServletRequest();
                                key.attach(servletRequest);
                            }else{
                                servletRequest = (NioServletRequest)key.attachment();
                            }
                            channel.read(servletRequest.getBuffer());
                            if (servletRequest.isFinished()){
                                //这里表示servletRequest构造完成，与Response一起放人contenxt，完成连接器的职责
                            }
                        } else{
                            throw new Exception("Error! A none acceptable key accepted");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
