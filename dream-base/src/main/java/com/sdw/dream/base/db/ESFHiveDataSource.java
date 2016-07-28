package com.sdw.dream.base.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.hive.jdbc.HiveDataSource;

public class ESFHiveDataSource extends HiveDataSource{
    
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    static{
        try {
            Class.forName(driverName);
          } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
          }
    }
    
    private String url;
    private String userName;
    private String password;
    
    public ESFHiveDataSource(Properties properties){
        this.url = properties.getProperty("url");
        this.userName = properties.getProperty("username");
        this.password = properties.getProperty("password");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.userName, this.password);
    }
    
    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return DriverManager.getConnection(this.url, username, password);
    }
}
