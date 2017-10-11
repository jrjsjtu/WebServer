package Connector;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jrj on 17-10-10.
 */
public class ReaderGroup {
    SingleReader[] Readers;
    AtomicInteger integer;
    int num;
    ReaderGroup(int num){
        integer = new AtomicInteger(0);
        Readers = new SingleReader[num];
        this.num = num;
    }

    public void register(SocketChannel channel){
        findNextReader().register(channel);
    }
    private SingleReader findNextReader(){
        return Readers[integer.getAndIncrement()%num];
    }
}
