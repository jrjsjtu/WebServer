package lifecycle;

/**
 * Created by jrj on 17-9-21.
 */
public interface Lifecycle {
    //wrapper，context，host，engine需要实现lifecycle接口
    void addLifecycleListener(LifecycleListener listener);
    void removeLifecycleListener(LifecycleListener listener);
    void start();
    void stop();
}
