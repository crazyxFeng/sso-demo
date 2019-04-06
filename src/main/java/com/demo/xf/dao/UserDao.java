package com.demo.xf.dao;

import com.demo.xf.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Author: xiongfeng
 * Date: 2019/4/5 12:22
 * Description:
 */
@Mapper
public interface UserDao {

    @Select("select * from user where user_name=#{userName}")
    User get(String userName);
}
