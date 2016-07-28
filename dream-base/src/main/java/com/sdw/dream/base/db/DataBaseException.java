package com.sdw.dream.base.db;

import java.sql.SQLException;

public class DataBaseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1431511749419561961L;
	private SQLException e;

	public DataBaseException(String msg) {
		this(msg, null);
	}

	public DataBaseException(String msg, SQLException e) {
		super(msg, e);
		this.e = e;
	}

	public DataBaseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public int getErrorCode() {
		if (e != null)
			return e.getErrorCode();
		else
			return -1;
	}
}
