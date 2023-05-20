package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.NotBoundException;
import myrmi.exception.RemoteException;
import myrmi.server.RemoteObjectRef;
import myrmi.server.Skeleton;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

public class RegistryImpl implements Registry {
    private final HashMap<String, Remote> bindings = new HashMap<>();

    /**
     * Construct a new RegistryImpl
     * and create a skeleton on given port
     **/
    public RegistryImpl(int port) throws RemoteException {
        Skeleton skeleton = new Skeleton(this, "127.0.0.1", port, 0);
        skeleton.start();
    }

    /**
     * Construct a new RegistryImpl
     * and define the host and port
     * */
    public RegistryImpl(String host, int port) throws RemoteException {
        Skeleton skeleton = new Skeleton(this, host, port, 0);
        skeleton.start();
    }


    public Remote lookup(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: lookup(%s)\n", name);
        //TODO: implement method here
        return bindings.get(name);
    }

    public void bind(String name, RemoteObjectRef obj) throws RemoteException, AlreadyBoundException {
        System.out.printf("RegistryImpl: bind(%s)\n", name);

        //TODO: implement method here
        bindings.put(name, obj);
    }

    public void unbind(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: unbind(%s)\n", name);

        //TODO: implement method here
        bindings.remove(name);

    }

    public void rebind(String name, RemoteObjectRef obj) throws RemoteException {
        System.out.printf("RegistryImpl: rebind(%s)\n", name);

        //TODO: implement method here
        bindings.put(name, obj);
    }

    public String[] list() throws RemoteException {
        //TODO: implement method here
        return bindings.keySet().toArray(new String[0]);
    }
}
