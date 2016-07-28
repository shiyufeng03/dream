package com.sdw.dream.webapp.common;

public class BaseController {

    protected <T> BaseRespose<T> buildRespose(T data){
        return BaseRespose.create(ReturnCode.Success.getCode(), ReturnCode.Success.getMessage(), data);
    }
    
    protected <T> BaseRespose<T> buildRespose(int code, String message, T data){
        return BaseRespose.create(code, message, data);
    }
    
    protected BaseRespose<Void> buildRespose(int code, String message){
        return BaseRespose.create(code, message);
    }
    
    protected BaseRespose<Void> buildRespose(){
        return BaseRespose.create(ReturnCode.Success.getCode(), ReturnCode.Success.getMessage());
    }
}
