package com.sdw.dream.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    
    public static HttpResponse get(String url, Map<String, Object> param) {
        HttpClient client = HttpClientBuilder.create().build();
        
        String paramString = buildParam(param);
        
        String tmpUrl = url + "?" + paramString;
        HttpGet request = new HttpGet(tmpUrl);

        try {
            HttpResponse response = client.execute(request);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String buildParam(Map<String, Object> param){
        StringBuffer sb = new StringBuffer();
        int count = 0;
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if(count > 0){
                sb.append("&");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            
            count++;
        }
        
        return sb.toString();
    }
    
    public static <T> T get(String url, Map<String, Object> param, Class<T> clz){
        try {
            HttpResponse response = get(url, param);

            String jsonResult = convertStreamToString(response.getEntity().getContent());
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                T result = JSON.parseObject(jsonResult, clz);
                return result;
            }else{
                LOGGER.warn("get error, errorMessage:" + jsonResult);
            }
            
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static HttpResponse post(String url, Map<String, Object> param){
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost  request = new HttpPost(url);

//        Header header = new 
        request.setHeader("Content-type", "application/json");
        
        
        
        try {
            String json = JSON.toJSONString(param);
            StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题    
            entity.setContentEncoding("UTF-8");    
            entity.setContentType("application/json");  
            request.setEntity(entity);
            
            HttpResponse response = client.execute(request);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T post(String url, Map<String, Object> param, Class<T> clz){
        try {
            HttpResponse response = post(url, param);

            String jsonResult = convertStreamToString(response.getEntity().getContent());
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                T result = JSON.parseObject(jsonResult, clz);
                
                return result;
            }else{
                LOGGER.warn("get error, errorMessage:" + jsonResult);
            }
            
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            LOGGER.error("read error.", e);
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
            }      
        }      
        return sb.toString();      
    }  

}
