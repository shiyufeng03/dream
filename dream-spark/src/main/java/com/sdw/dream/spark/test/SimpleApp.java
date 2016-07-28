package com.sdw.dream.spark.test;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

import com.sdw.dream.base.Config;
import com.sdw.dream.base.Path;

public class SimpleApp {
  public static void main(String[] args) {
    String logFile = Path.combine(Config.getConfigDir(), "readme.txt"); // Should be some file on your system
    SparkConf conf = new SparkConf().setAppName("Simple Application").setMaster("local");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> logData = sc.textFile(logFile).cache();

    long numAs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("a"); }
    }).count();

    long numBs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("b"); }
    }).count();

    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
  }
}