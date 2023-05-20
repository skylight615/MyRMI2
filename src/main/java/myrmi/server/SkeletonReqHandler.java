package myrmi.server;

import myrmi.Remote;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import myrmi.registry.RegistryImpl;
import myrmi.registry.RegistryStubInvocationHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.ArrayList;

public class SkeletonReqHandler extends Thread {
    private Socket socket;
    private Remote obj;
    private int objectKey;

    public SkeletonReqHandler(Socket socket, Remote remoteObj, int objectKey) {
        this.socket = socket;
        this.obj = remoteObj;
        this.objectKey = objectKey;
    }

    @Override
    public void run() {
        int objectKey;
        Method method;
        Class<?>[] argTypes;
        ArrayList<Object> args = new ArrayList<>();
        Object result;

        /*TODO: implement method here
         * You need to:
         * 1. handle requests from stub, receive invocation arguments, deserialization
         * 2. get result by calling the real object, and handle different cases (non-void method, void method, method throws exception, exception in invocation process)
         * Hint: you can use an int to represent the cases: -1 invocation error, 0 exception thrown, 1 void method, 2 non-void method
         *
         *  */
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            // accept the method and parameters
            String name =(String) in.readObject();
            System.out.println("server run the method: "+name);
            Object[] arg = (Object[]) in.readObject();
            argTypes = new Class[arg.length];
            int i = 0;
            for (Object o: arg){
                argTypes[i++] = o.getClass();
            }
//            System.out.println("server: get input");
            method = obj.getClass().getMethod(name, argTypes);
            result = method.invoke(obj, arg);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            if (result != null){
                out.writeObject(result);
            }
//            System.out.println("server: give back reply");
            out.flush();
            socket.close();
        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
