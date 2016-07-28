package com.sdw.dream.base.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;


public class DataBaseFactory {
    private DataBaseFactory() {
    }

    private static DataBaseFactory factory = new DataBaseFactory();

    private final static String MySqlConfigFile = "mip_mysql.xml";

    /**
     * @return the factory
     */
    public static DataBaseFactory getFactory() {
        return factory;
    }

    static Logger _logger = Logger.getLogger(DataBaseFactory.class);

    private HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

    private DataSource dataSource;

    public DataBase createDataBase() throws DataBaseException {
        InputStream stream = null;
        Properties properties = new Properties();
        try {
            if (dataSource == null) {
                ClassPathResource path = new ClassPathResource(MySqlConfigFile);
                stream = new FileInputStream(path.getFile());
                properties.loadFromXML(stream);
                dataSource = BasicDataSourceFactory.createDataSource(properties);
            }
            return new DataBase(dataSource);
        } catch (IOException e) {
            throw new DataBaseException("mysql config file is error,pls add " + MySqlConfigFile, e);
        } catch (Exception e) {
            String message = String.format("path:%s data create fail.", MySqlConfigFile);
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    String message = String.format("path:%s file io exception.", MySqlConfigFile);
                    _logger.error(message, e);
                    throw new DataBaseException(message, e);
                }
            }
        }
    }

    public DataBase createDataBase(File file) throws DataBaseException {
        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            properties.loadFromXML(stream);
            
            return createDataBase(file.getPath(), properties);

        } catch (FileNotFoundException e) {
            String message = String.format("path:%s file not found.", file.getPath());
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        } catch (InvalidPropertiesFormatException e) {
            String message = String.format("path:%s file properties format exception.", file.getPath());
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        } catch (IOException e) {
            String message = String.format("path:%s file io exception.", file.getPath());
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        } catch (Exception e) {
            String message = String.format("path:%s data create fail.", file.getPath());
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                String message = String.format("path:%s file io exception.", file.getPath());
                _logger.error(message, e);
                throw new DataBaseException(message, e);
            }
        }

    }

    public DataBase createDataBase(String dataSourceKey, Properties properties) throws DataBaseException {
        DataSource dataSource = dataSources.get(dataSourceKey);
        if (dataSource != null)
            return new DataBase(dataSource);

        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
            dataSources.put(dataSourceKey, dataSource);
            return new DataBase(dataSource);
        } catch (Exception e) {
            String message = String.format("dataSourceKey:%s data create fail.", dataSourceKey);
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        }

    }
    
    public DataBase createHiveDataBase(String dataSourceKey, Properties properties) throws DataBaseException {
        DataSource dataSource = dataSources.get(dataSourceKey);
        if (dataSource != null)
            return new DataBase(dataSource);

        try {
            dataSource = new ESFHiveDataSource(properties);
            dataSources.put(dataSourceKey, dataSource);
            return new DataBase(dataSource);
        } catch (Exception e) {
            String message = String.format("dataSourceKey:%s data create fail.", dataSourceKey);
            _logger.error(message, e);
            throw new DataBaseException(message, e);
        }

    }

}
