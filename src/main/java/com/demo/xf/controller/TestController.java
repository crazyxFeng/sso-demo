package com.demo.xf.controller;

import com.demo.xf.exception.BizException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 10:32
 * Description:
 */
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping("/test1")
    public String test1() {
        return "test1";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/test2")
    public String test2() {
        return "test2";
    }
}
