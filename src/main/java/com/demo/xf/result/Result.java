package com.demo.xf.result;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 13:29
 * Description:
 */
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private String errorMessage;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public Result(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    private Result(T data) {
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.message = Status.SUCCESS.code();
        this.errorMessage = "";
    }

    private Result(){
        this.code = HttpStatus.OK.value();
        this.message = Status.SUCCESS.code();
    }

    public static <T> Result<T> success(T data){
        return new Result<>(data);
    }

    public static Result<Void> success(){
        return new Result<>();
    }

    public static Result error(int code, String errorMessage){
        return new Result(code, errorMessage);
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public enum Status{
        SUCCESS("SUCCESS"),
        FAILED("FAILED"),
        ERROR("ERROR"),
        ;

        private String code;

        Status(String code) {
            this.code = code;
        }
        public String code(){
            return this.code;
        }
    }
}
