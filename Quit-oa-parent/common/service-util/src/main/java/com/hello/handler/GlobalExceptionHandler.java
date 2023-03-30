package com.hello.handler;

import com.hello.exception.SystemException;
import com.hello.result.Result;
import com.hello.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return Result.errorResult(ResultCodeEnum.SYSTEM_ERROR.getCode(),
                e.getMessage());
    }
    @ExceptionHandler(SystemException.class)
    public Result systemExceptionHandler(SystemException e){
        log.error("出现了异常！ {}",e);
        return Result.errorResult(e.getCode(),e.getMsg());
    }
    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }
}