package Connector;

import containers.Context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Created by jrj on 17-10-10.
 */
public class ConnectionAccptor implements Runnable{
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private Context context;
    private ReaderGroup readerGroup;
    ConnectionAccptor(int port,Context context){
        try {
            this.context = context;
            selector =  SelectorProvider.provider().openSelector();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            readerGroup = new ReaderGroup(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
            while(true){
                int keys = selector.select();
                if (keys > 0) {
                    Iterator selectorKeys = selector.selectedKeys().iterator();
                    while (selectorKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectorKeys.next();
                        selectorKeys.remove();
                        if (key.isAcceptable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            readerGroup.register(channel);
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
