package com.sdw.dream.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    public static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    
    private Properties properties;
    private String propFileName;
    public Config(String propFileName){
        this.propFileName = propFileName;
        this.init();
    }
    
    private void init(){
        Properties props = new Properties();
        String filePath = getConfigDir() + "/" + this.propFileName;
        InputStream in = null;
        try {
            in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            
            this.properties = props;
        } catch (IOException e) {
           throw new RuntimeException("load properties error, fileName:" + filePath, e);
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    private static String getUserDir(){
        return System.getProperty("user.dir");
    }
    
    private static final String ML_DREAM_CONFIG_NAME = "ML_DREAM_CONFIG";
    private static String ML_DREAM_CONFIG_PATH;
    private static Object lock = new Object();
    public static String getConfigDir(){
        if(ML_DREAM_CONFIG_PATH == null){
            synchronized (lock) {
                if(ML_DREAM_CONFIG_PATH == null){
                    String config;
                    
                    config = System.getenv(ML_DREAM_CONFIG_NAME);
                    if(config == null){
                        String userDir = getUserDir();
                        File file = new File(userDir);
                        String parent = file.getParent();
                        
                        config = parent + "/config";
                        File readMe = new File(config + "/readme.txt");
                        if(readMe.exists() == false){
                            throw new RuntimeException("readme.txt not found, path:" + readMe.getAbsolutePath());
                        }else{
                            LOGGER.info(ML_DREAM_CONFIG_NAME + ":" + config + ", source:user.dir property.");
                        }
                    }else{
                        LOGGER.info(ML_DREAM_CONFIG_NAME + ":" + config + ", source:ML_CONFIG environment.");
                    }
                    
                    ML_DREAM_CONFIG_PATH = config;
                }
            }
        }
        
        return ML_DREAM_CONFIG_PATH;
    }
    
    public String getProperties(String key, String defaultValue){
        return this.properties.getProperty(key, defaultValue);
    }
    
    public double getProperties(String key, double defaultValue){
        String val = this.properties.getProperty(key);
        if(val == null){
            return defaultValue;
        }
        return Double.parseDouble(val);
    }
}
