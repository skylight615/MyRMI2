package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.RemoteException;
import myrmi.exception.NotBoundException;
import myrmi.server.RemoteObjectRef;

import java.util.HashMap;

public interface Registry extends Remote {
    int REGISTRY_PORT = 11099;
    String host = "127.0.0.1";
    HashMap<String, Remote> bindings = new HashMap<>();

    public Remote lookup(String name) throws RemoteException, NotBoundException;

    public void bind(String name, RemoteObjectRef obj) throws RemoteException, AlreadyBoundException;

    public void unbind(String name) throws RemoteException, NotBoundException;

    public void rebind(String name, RemoteObjectRef obj) throws RemoteException;

    public String[] list() throws RemoteException;


}
