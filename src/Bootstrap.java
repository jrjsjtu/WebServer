import Connector.ConnectionAccptor;
import containers.StandardContext;

/**
 * Created by jrj on 17-10-12.
 */
public class Bootstrap {
    public static void main(String[] args){
        int port = 10103;
        new ConnectionAccptor(10103,new StandardContext()).run();
    }
}
