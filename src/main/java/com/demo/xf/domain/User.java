package com.demo.xf.domain;

import java.io.Serializable;

/**
 * Author: xiongfeng
 * Date: 2019/4/5 12:10
 * Description:
 */
public class User implements Serializable {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
