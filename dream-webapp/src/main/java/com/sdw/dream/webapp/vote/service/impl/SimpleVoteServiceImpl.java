package com.sdw.dream.webapp.vote.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.sdw.dream.webapp.vote.model.RequestResult;
import com.sdw.dream.webapp.vote.service.SimpleVoteService;

public class SimpleVoteServiceImpl implements SimpleVoteService{

	@Override
	public RequestResult vote(String rquestUrl) {
		Connection conn = Jsoup.connect(rquestUrl)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:42.0) Gecko/20100101 Firefox/42.0")
				.proxy("221.206.154.23", 3128)
//		        .referrer("http://www.toutoupiao.com/Vote/15137")
				;
		
		try {
			Response response = conn.ignoreContentType(true).method(Method.GET).execute();
			System.out.println(response.body());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void voteBatch(String requestUrl, int times){
		
		Connection conn = Jsoup.connect(requestUrl)
		.header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:42.0) Gecko/20100101 Firefox/42.0")
		.proxy("221.206.154.23", 3128)
        .referrer("http://www.toutoupiao.com/Vote/15137");
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("Choice", "78549");
		data.put("X-Requested-With", "XMLHttpRequest");
		data.put("__RequestVerificationToken", "Me2r9dsaIJlRHYp-4EobjxHTN6NAOhtPk0T-tXnZk1gNDi13zpOyzyhI11nrLCvUvebXd2yyxTinvXqvQSBHJfGAqU6oBA2RugR-H6iMgIo1");
		
		try {
			Response response = conn.ignoreContentType(true).method(Method.POST).data(data).execute();
			
			System.out.println(response.body().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SimpleVoteService vote = new SimpleVoteServiceImpl();
		
		/*String requestUrl = "http://www.toutoupiao.com/Ajax/SendVote/15137";
		vote.voteBatch(requestUrl, 10);*/
		
		vote.vote("http://www.baidu.com");
	}

}
