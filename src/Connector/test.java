package Connector;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Created by jrj on 17-9-12.
 */
public class test {
    ThreadLocal<Integer> testTHreadLocal;
    test(){
        testTHreadLocal = new ThreadLocal<Integer>();
        testTHreadLocal.set(0);
        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testTHreadLocal.set(1);
                    System.out.println(testTHreadLocal.get());
                }
            }).start();
        }
        System.out.println(testTHreadLocal.get());
    }
    public static void main(String[] args) {
        test sss = new test();
    }


}
