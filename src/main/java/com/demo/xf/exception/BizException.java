package com.demo.xf.exception;

import java.util.List;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 13:21
 * Description:
 */
public class BizException extends RuntimeException {
    private int code;
    private String message;
    private List<String> messages;
    private Object content;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public BizException setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public Object getContent() {
        return content;
    }

    public BizException setContent(Object content) {
        this.content = content;
        return this;
    }

}
