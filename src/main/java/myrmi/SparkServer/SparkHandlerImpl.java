package myrmi.SparkServer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.TimeZone;

import static org.apache.spark.sql.functions.*;

public class SparkHandlerImpl implements SparkHandler {
    @Override
    public List<Row> getDataset(String name) {
        System.out.println("run the getDataset");
        String logFile = "/opt/spark/work-dir/src/main/java/myrmi/SparkServer/" + name;
        System.out.println("The path" + logFile);
        SparkSession spark = SparkSession.builder().appName("Simple Application").master("local").getOrCreate();
        System.out.println("milestone 1");
        Dataset<Row> sourceData = spark.read().format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .option("inferSchema", "true")
                .load(logFile);
        System.out.println("milestone 2");
        return sourceData.filter(sourceData.col("in_time").$less(sourceData.col("out_time"))).collectAsList();
    }

    @Override
    public Dataset<Row> task1(Dataset<Row> sourceData) {
        return sourceData.dropDuplicates("berthage", "section").groupBy("section").agg(count("*").alias("count"));
    }

    @Override
    public Dataset<Row> task2(Dataset<Row> sourceData) {
        return sourceData.dropDuplicates("berthage", "section").select("berthage", "section");
    }

    @Override
    public Dataset<Row> task3(Dataset<Row> sourceData) {
        return sourceData.withColumn("parking_time", unix_timestamp(col("out_time")).minus(unix_timestamp(col("in_time"))))
                .groupBy("section").agg(round(avg("parking_time"), 0).alias("avg_parking_time"));
    }

    @Override
    public Dataset<Row> task4(Dataset<Row> sourceData) {
        return sourceData.withColumn("parking_time", unix_timestamp(col("out_time")).minus(unix_timestamp(col("in_time"))))
                .groupBy("berthage").agg(round(avg("parking_time"), 0).alias("avg_parking_time"));
    }

    @Override
    public Dataset<Row> task5(Dataset<Row> sourceData) {
        long totalNumber = sourceData.select("berthage").dropDuplicates().count();
        Dataset<Row> task5 = sourceData.withColumn("in_time_unit", unix_timestamp(col("in_time"))
                        .minus(minute(col("in_time")).mod(30).multiply(60))
                        .minus(second(col("in_time"))))
                .withColumn("length", floor(unix_timestamp(col("out_time")).minus(col("in_time_unit")).divide(30 * 60)))
                .withColumn("loop", expr("split(repeat(':', length), ':')"))
                .select(col("*"), posexplode(col("loop")))
                .withColumn("start_time", from_utc_timestamp(to_timestamp(col("in_time_unit").plus(col("pos").multiply(1800))), TimeZone.getTimeZone("UTC").getID()));
        Dataset<Row> countNum = task5.groupBy("section", "start_time").agg(count("*").alias("count"));
        task5 = task5.join(countNum, countNum.col("start_time").equalTo(task5.col("start_time"))
                        .and(countNum.col("section").equalTo(task5.col("section"))))
                .drop(task5.col("start_time")).drop(task5.col("section"))
                .withColumn("end_time", from_utc_timestamp(to_timestamp(unix_timestamp(col("start_time")).plus(1800)), TimeZone.getTimeZone("UTC").getID()))
                .withColumn("percentage", round(col("count").divide(totalNumber).multiply(100), 2))
                .select("start_time", "end_time", "section", "count", "percentage")
                .dropDuplicates()
                .withColumn("start_time", date_format(col("start_time"), "yyyy-MM-dd HH:mm:ss"))
                .withColumn("end_time", date_format(col("end_time"), "yyyy-MM-dd HH:mm:ss"))
                .orderBy(unix_timestamp(col("start_time")));
        return task5;
    }
}
