package com.demo.xf.service.impl;

import com.demo.xf.dao.UserDao;
import com.demo.xf.domain.User;
import com.demo.xf.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Author: xiongfeng
 * Date: 2019/4/5 12:26
 * Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public User get(String username) {
        return userDao.get(username);
    }
}
