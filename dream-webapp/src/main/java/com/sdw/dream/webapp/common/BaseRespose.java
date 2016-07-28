package com.sdw.dream.webapp.common;

public class BaseRespose<T> {
    private int code;
    private String message;
    private T data;

    private BaseRespose(){
        this(ReturnCode.Success.getCode(), ReturnCode.Success.getMessage(), null);
    }
    
    private BaseRespose(T data){
        this(ReturnCode.Success.getCode(), ReturnCode.Success.getMessage(), data);
    }
    
    private BaseRespose(int code, String message){
        this(code, message, null);
    }
    
    private BaseRespose(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> BaseRespose<T> create(int code, String message, T data){
        return new BaseRespose<T>(code, message, data);
    }
    
    public static BaseRespose<Void> create(int code, String message){
        return new BaseRespose<Void>(code, message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
