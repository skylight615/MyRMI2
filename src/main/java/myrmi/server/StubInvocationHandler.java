package myrmi.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StubInvocationHandler implements InvocationHandler, Serializable {
    private String host;
    private int port;
    private int objectKey;

    public StubInvocationHandler(String host, int port, int objectKey) {
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        System.out.printf("Thread%d:Stub created to %s:%d, object key = %d\n", Thread.currentThread().getId(),host, port, objectKey);
    }

    public StubInvocationHandler(RemoteObjectRef ref) {
        this(ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*TODO: implement stub proxy invocation handler here
         *  You need to do:
         * 1. connect to remote skeleton, send method and arguments
         * 2. get result back and return to caller transparently
         * */
        Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(method.getName());
        out.writeObject(args);
        out.flush();
        System.out.println("client: finish output");
        Object result = null;
        if (!method.getReturnType().equals(Void.TYPE)){
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            result = in.readObject();
        }
        System.out.println("client: get reply");
        return result;
    }
}
