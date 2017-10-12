package Connector;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jrj on 17-10-10.
 */
public class ReaderGroup {
    SingleReader[] readers;
    AtomicInteger integer;
    int num;
    ReaderGroup(int num){
        integer = new AtomicInteger(0);
        readers = new SingleReader[num];
        for (int i=0;i<num;i++){
            readers[i] = new SingleReader();
        }
        this.num = num;
    }

    public void register(SocketChannel channel){
        SingleReader singleReader = findNextReader();
        singleReader.execute(()->singleReader.register(channel));
    }

    private SingleReader findNextReader(){
        return readers[integer.getAndIncrement()%num];
    }
}
