package com.sdw.dream.webapp.common;

public enum ReturnCode {
    
    Success(0, "success"),
    
    SYSTEM_ERROR(10, "系统出错"),
    EMPTY_PARAMETER(11, "缺少参数"),
    INVALID_PARAMETER(12, "参数无效"),
    
    OTHER_ERROR(99, "其他未知错误");
    
    private final int code;
    private final String message;
    
    private ReturnCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
