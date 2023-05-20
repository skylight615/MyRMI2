package myrmi.SparkServer;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.RemoteException;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import myrmi.server.RemoteObjectRef;
import myrmi.server.UnicastRemoteObject;
public class SparkServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 11111);
        SparkHandlerImpl spark = new SparkHandlerImpl();
        Remote service = UnicastRemoteObject.exportObject(spark, 9999);
        registry.bind("SparkService", new RemoteObjectRef("127.0.0.1", 9999,
                spark.hashCode(), "myrmi.SparkServer.SparkHandler"));
    }
}
