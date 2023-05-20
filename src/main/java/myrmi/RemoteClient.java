package myrmi;

import myrmi.SparkServer.SparkHandler;
import myrmi.exception.NotBoundException;
import myrmi.exception.RemoteException;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public class RemoteClient {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",11111);
        System.out.println("looking for the spark service");
        // get a stub for subService
        SparkHandler spark = (SparkHandler) registry.lookup("SparkService");
        List<Row> cleanedData = spark.getDataset("parking_data_sz.csv");
        System.out.println("arrive");
        System.out.println(cleanedData.size());
//        Dataset<Row> taskResult = spark.task1(cleanedData);
//        System.out.println(taskResult);
//        taskResult.show();
    }
}
