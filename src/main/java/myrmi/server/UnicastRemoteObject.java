package myrmi.server;

import myrmi.Remote;
import myrmi.exception.RemoteException;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import myrmi.registry.RegistryStubInvocationHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UnicastRemoteObject implements Remote, java.io.Serializable {
    int port;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int port) throws RemoteException {
        this.port = port;
        exportObject(this, port);
    }

    public static Remote exportObject(Remote obj) throws RemoteException {
        return exportObject(obj, 0);
    }

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj, "127.0.0.1", port);
    }

    /**
     * 1. create a skeleton of the given object ``obj'' and bind with the address ``host:port''
     * 2. return a stub of the object ( Util.createStub() )
     **/
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static Remote exportObject(Remote obj, String host, int port) throws RemoteException {
        //TODO: finish here
        Class<?> implClass = obj.getClass();
        Class<?>[] interfaces = implClass.getInterfaces();
        Optional<Class<?>> cls = Arrays.stream(interfaces).filter(Remote.class::isAssignableFrom).findAny();
        if (!cls.isPresent()){
            throw new RemoteException();
        }
        Skeleton s = new Skeleton(obj, host, port, obj.hashCode());
        // wake up a thread to run new service
        threadPool.submit(s);
        return Util.createStub(new RemoteObjectRef(host, port, obj.hashCode(), cls.get().getName()));
    }
}
