package myrmi.SparkServer;

import myrmi.Remote;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.File;
import java.util.List;

public interface SparkHandler extends Remote {

    List<Row> getDataset(String name);
    Dataset<Row> task1(Dataset<Row> sourceData);
    Dataset<Row> task2(Dataset<Row> sourceData);
    Dataset<Row> task3(Dataset<Row> sourceData);
    Dataset<Row> task4(Dataset<Row> sourceData);
    Dataset<Row> task5(Dataset<Row> sourceData);
}
