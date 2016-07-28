package com.sdw.dream.base.db;


public class DataReadException extends DataBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 617862408844899300L;

	public DataReadException(String ctxMsg, String message, Exception exp) {
		super(String.format("[%s]%s", ctxMsg, message), exp);
	}
}
