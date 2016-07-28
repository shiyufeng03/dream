package com.sdw.dream.webapp.vote.service;

import com.sdw.dream.webapp.vote.model.RequestResult;

public interface SimpleVoteService {

	public RequestResult vote(String rquestUrl);
	
	public void voteBatch(String requestUrl, int times);
}
