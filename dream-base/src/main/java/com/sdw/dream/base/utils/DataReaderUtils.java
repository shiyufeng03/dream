package com.sdw.dream.base.utils;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.sdw.dream.base.db.DataBase;
import com.sdw.dream.base.db.DataBaseFactory;
import com.sdw.dream.base.db.DataReader;


public class DataReaderUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);
    
    public static DataReader getDataReader(String sql, String fileName) {
        Properties properties = new Properties();
        try {
//            String fileName = "esf-user.properties";
            Resource resource = new ClassPathResource(fileName);
            InputStream in = resource.getInputStream();
            properties.load(in);
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info(properties.toString());

        DataBaseFactory factory = DataBaseFactory.getFactory();
        DataBase dataBase = factory.createDataBase(properties.getProperty("url"), properties);

        return dataBase.executeQuery(sql);
    }
}
