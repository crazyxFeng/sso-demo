package com.demo.xf.advice;

import com.demo.xf.exception.BizException;
import com.demo.xf.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 13:26
 * Description:
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(BizException.class)
    public Result handleException(BizException e){
        LOGGER.error(">>> 业务异常：" + e.getMessage(), e);
        return new Result(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        LOGGER.error(">>> 未知异常信息：" + e.getMessage(), e);
        return new Result<>(500, "未知异常信息");
    }
}
