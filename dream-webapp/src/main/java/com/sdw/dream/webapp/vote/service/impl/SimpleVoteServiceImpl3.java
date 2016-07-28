package com.sdw.dream.webapp.vote.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.sdw.dream.webapp.vote.model.RequestResult;
import com.sdw.dream.webapp.vote.service.SimpleVoteService;

public class SimpleVoteServiceImpl3 implements SimpleVoteService {

	@Override
	public RequestResult vote(String rquestUrl) {
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(rquestUrl, 80,  
                "http");  
		
		// 依次是代理地址，代理端口号，协议类型  
        HttpHost proxy = new HttpHost("221.206.154.23", 3128, "http");  
        RequestConfig config = RequestConfig.custom()
        		.setProxy(proxy)
        		.build();  
		
     // 请求地址  
        HttpPost httpPost = new HttpPost(rquestUrl);  
        httpPost.setConfig(config);
        
        httpPost.setHeader("Accept", "*/*");    
        
        httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");    
    
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");    
    
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");    
    
        httpPost.setHeader("Connection", "keep-alive");    
    
        httpPost.setHeader("Cookie", "_ga=GA1.2.902581532.1462782240; __RequestVerificationToken=mzk_1sm3-EBg0nT6qQ8kM7NSwI1quWhzhLbCETfuLvsObLbr6r6iD42t4DON_Ny-MNjmUDKNK6zh2CK5hJENGOLcqIBSX-1UWgU9PAXNAJU1; 15137=78549; _gat=1");    
    
        httpPost.setHeader("Host", "www.toutoupiao.com");    
    
        httpPost.setHeader("refer", "http://www.toutoupiao.com/Vote/15071");    
    
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        
     // 创建参数队列  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        // 参数名为pid，值是2  
        formparams.add(new BasicNameValuePair("Choice", "77957"));  
        formparams.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));  
        formparams.add(new BasicNameValuePair("__RequestVerificationToken", "UqPrlonnUbF7iKYahBd9N5BLMzloCB7ac1qhqfsPPU4zUj-wv7t9Diu3ayKliTj_ZYWRnhU3YJNQejw9aQ2UX1-UrAYA9lyu2lC_qMKayPc1"));  
        
        UrlEncodedFormEntity entity;  
        try {  
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = closeableHttpClient.execute(  
                    target, httpPost);  
            // getEntity()  
            HttpEntity httpEntity = response.getEntity();  
            if (httpEntity != null) {  
                // 打印响应内容  
                System.out.println("response:"  
                        + EntityUtils.toString(httpEntity, "UTF-8"));  
            }  
            // 释放资源  
            closeableHttpClient.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
		return null;
	}
	
	
	public void voteGet(String requestUrl){
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(requestUrl, 80,  
                "http");  
		
		// 依次是代理地址，代理端口号，协议类型  
        HttpHost proxy = new HttpHost("221.206.154.23", 3128, "http");  
        RequestConfig config = RequestConfig.custom()
        		.setProxy(proxy)
        		.build();  
        
        HttpGet httpGet = new HttpGet(requestUrl);
        httpGet.setConfig(config);
        
        try {  
            CloseableHttpResponse response = closeableHttpClient.execute(  
                    target, httpGet);  
            // getEntity()  
            HttpEntity httpEntity = response.getEntity();  
            if (httpEntity != null) {  
                // 打印响应内容  
                System.out.println("response:"  
                        + EntityUtils.toString(httpEntity, "UTF-8"));  
            }  
            // 释放资源  
            closeableHttpClient.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        
	}

	@Override
	public void voteBatch(String requestUrl, int times) {

	}

	public static void main(String[] args) {
		SimpleVoteServiceImpl3 vote = new SimpleVoteServiceImpl3();

		String rquestUrl = "www.toutoupiao.com/Ajax/SendVote/15071";
		vote.vote(rquestUrl);
		
//		vote.voteGet(rquestUrl);
	}
}
