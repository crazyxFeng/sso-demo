package com.demo.xf.exception;

import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 14:02
 * Description:
 */
public class CustomAccessDeniedException extends AccessDeniedException {

    private int code;
    private String message;
    private List<String> messages;
    private Object content;

    public CustomAccessDeniedException(String msg) {
        super(msg);
    }
    public CustomAccessDeniedException(int code, String message) {
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

    public CustomAccessDeniedException  setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public Object getContent() {
        return content;
    }

    public CustomAccessDeniedException setContent(Object content) {
        this.content = content;
        return this;
    }
}
