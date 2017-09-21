package lifecycle;

/**
 * Created by jrj on 17-9-21.
 */
public interface Lifecycle {
    void addLifecycleListener(LifecycleListener listener);
    void removeLifecycleListener(LifecycleListener listener);
    void start();
    void stop();
}
