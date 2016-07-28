package com.sdw.dream.base;

import java.io.File;

import org.apache.commons.lang3.StringUtils;


public class Path {
	private final static String PATH_SPLIT = "/";

	/*
	 * get directory path
	 * 
	 * @param path file or directory path
	 * 
	 * @return directory path
	 */

	public static String getDirectoryPath(String path) {
		File file = new File(path);
		return file.getAbsolutePath();
	}

	/*
	 * combine path1 and path2
	 * 
	 * @param path1 before path
	 * 
	 * @param path2 end path
	 * 
	 * @return combine path
	 */

	public static String combine(String path1, String path2) {
		if(StringUtils.isEmpty(path1) && StringUtils.isEmpty(path2))
			throw new IllegalArgumentException("path1 or path2 must be at least one non-empty.");
		else if (StringUtils.isEmpty(path1)) {
			return path2;
		} else if (StringUtils.isEmpty(path2)) {
			return path1;
		} else if (path1.endsWith(PATH_SPLIT) && path2.startsWith(PATH_SPLIT)) {
			return path1 + path2.substring(1);
		} else if (path1.endsWith(PATH_SPLIT) || path2.startsWith(PATH_SPLIT)) {
			return path1 + path2;
		} else {
			return path1 + PATH_SPLIT + path2;
		}
	}
	
}
