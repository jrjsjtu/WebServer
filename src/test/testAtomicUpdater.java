package test;

/**
 * Created by jrj on 17-10-12.
 */
public class testAtomicUpdater {
    volatile int test = 0;
    public int getResult(){
        System.out.println(this);
        return test;
    }
}
