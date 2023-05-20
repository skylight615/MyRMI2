package myrmi.server;

import myrmi.Remote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Util {


    public static Remote createStub(RemoteObjectRef ref) {
        //TODO: finish here, instantiate an StubInvocationHandler for ref and then return a stub
        Class<?> cls = null;
        try {
            cls = Class.forName(ref.getInterfaceName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Remote) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new StubInvocationHandler(ref));
    }

}
