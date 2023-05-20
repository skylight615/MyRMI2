package myrmi.server;

import myrmi.Remote;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Skeleton extends Thread {
    static final int BACKLOG = 5;
    private Remote remoteObj;

    private String host;
    private int port;
    private int objectKey;

    public int getPort() {
        return port;
    }

    public Skeleton(Remote remoteObj, RemoteObjectRef ref) {
        this(remoteObj, ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    public Skeleton(Remote remoteObj, String host, int port, int objectKey) {
        super();
        this.remoteObj = remoteObj;
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        this.setDaemon(false);
    }

    //each skeleton instance will have a threadPool to manage the request connection
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void run() {
        /*TODO: implement method here
         * You need to:
         * 1. create a server socket to listen for incoming connections
         * 2. use a handler thread to process each request (use SkeletonReqHandler)
         *  */
        try (ServerSocket ss = new ServerSocket(port)) {
//            Registry service = LocateRegistry.getRegistry(host, port);
            while (true){
                Socket c = ss.accept();
                SkeletonReqHandler srh = new SkeletonReqHandler(c, remoteObj, objectKey);
                threadPool.submit(srh);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
