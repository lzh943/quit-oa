package com.hello.exception;


import com.hello.result.ResultCodeEnum;

public class SystemException extends RuntimeException{

    private int code;

    private String message;
    public SystemException(int code,String message){
        super(message);
        this.code=code;
        this.message=message;
    }
    public int getCode(){
        return code;
    }
    public String getMsg(){
        return message;
    }
    public SystemException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
    @Override
    public String toString() {
        return "SystemException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}