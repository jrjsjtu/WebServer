package Connector;

import containers.Context;
import request.NioServletRequest;
import response.NioServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponseWrapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by jrj on 17-10-10.
 */
public class SingleReader implements Runnable{
    private volatile int status;
    private static int THREADNOTSTART = 0;
    private static int THREADSTARTED = 1;
    private static AtomicIntegerFieldUpdater atomicIntegerFieldUpdater =  AtomicIntegerFieldUpdater.newUpdater(SingleReader.class, "status" );
    Selector selector;
    LinkedBlockingQueue<Runnable> taskQueue;
    Thread currentThread;
    Context context;
    SingleReader(Context context){
        try {
            this.context = context;
            selector = SelectorProvider.provider().openSelector();
            taskQueue = new LinkedBlockingQueue<>();
            status = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void register(SocketChannel channel){
        try {
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void execute(Runnable task){
        try {
            taskQueue.put(task);
            selector.wakeup();
            if (atomicIntegerFieldUpdater.get(this)==THREADNOTSTART){
                atomicIntegerFieldUpdater.compareAndSet(this,THREADNOTSTART,THREADSTARTED);
                currentThread = new Thread(this);
                currentThread.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try{
            while(true){
                int keys = selector.select(5000);
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
                            int byteNum = channel.read(servletRequest.getBuffer());
                            if (byteNum == 0){
                                channel.finishConnect();
                                continue;
                            }
                            if (servletRequest.isFinished()){
                                context.invoke(servletRequest,new NioServletResponse(channel));
                                //这里要是没有keepAlive就可以直接不attach新的request？
                                key.attach(new NioServletRequest());
                                System.out.println(servletRequest.getRequestURI());
                                //这里表示servletRequest构造完成，与Response一起放人contenxt，完成连接器的职责
                            }
                        } else{
                            throw new Exception("Error! A none acceptable key accepted");
                        }
                    }
                }
                while (taskQueue.size()>0){
                    taskQueue.take().run();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
