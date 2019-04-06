package com.demo.xf.exception;

import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 14:03
 * Description:
 */
public class CustomAuthenticationException extends AuthenticationException {
    private int code;
    private String message;
    private List<String> messages;
    private Object content;

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
    public CustomAuthenticationException(int code, String message) {
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

    public CustomAuthenticationException  setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public Object getContent() {
        return content;
    }

    public CustomAuthenticationException setContent(Object content) {
        this.content = content;
        return this;
    }

}
