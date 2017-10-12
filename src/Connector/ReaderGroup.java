package Connector;

import containers.Context;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jrj on 17-10-10.
 */
public class ReaderGroup {
    SingleReader[] readers;
    AtomicInteger integer;
    Context context;
    int num;
    ReaderGroup(int num,Context context){
        integer = new AtomicInteger(0);
        readers = new SingleReader[num];
        this.context =context;
        for (int i=0;i<num;i++){
            readers[i] = new SingleReader(context);
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
