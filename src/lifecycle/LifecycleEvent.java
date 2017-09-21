package lifecycle;

import java.util.EventObject;

/**
 * Created by jrj on 17-9-21.
 */
public class LifecycleEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LifecycleEvent(Object source) {
        super(source);
    }
}
