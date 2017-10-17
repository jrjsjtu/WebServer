package test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by jrj on 17-10-12.
 */
public class Entrance {
    AtomicIntegerFieldUpdater<testAtomicUpdater> intupdater = AtomicIntegerFieldUpdater.newUpdater(testAtomicUpdater.class, "test" );
    public void increment(testAtomicUpdater tester){
        System.out.println(this);
        tester.getResult();
    }
    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
        Entrance entrance = new Entrance();
        testAtomicUpdater myUpdater = new testAtomicUpdater();
        entrance.increment(myUpdater);
        myUpdater.getResult();
    }
}
