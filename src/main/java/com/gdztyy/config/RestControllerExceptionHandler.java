package com.gdztyy.config;

import cn.hutool.core.util.ObjectUtil;
import com.gdztyy.api.vo.ResponseJson;
import com.gdztyy.exception.IncaApiRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peiqy
 */
@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler{

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object handleControllerException(RuntimeException e) {
        log.error("RuntimeException", e);
        return new ResponseEntity<>(ResponseJson.builder().errorCode("500").errorMessage(
                log.isDebugEnabled() ? e.getMessage() : "inner error"
        ).build(), HttpStatus.OK);
    }


    @ExceptionHandler(IncaApiRuntimeException.class)
    @ResponseBody
    public Object handleZSYCRuntimeExceptionControllerException(IncaApiRuntimeException e) {
        log.error("IncaApiRuntimeException", e);
        String code = e.getCode();
        code = ObjectUtil.isEmpty(code) ? "600" : code;
        return new ResponseEntity<>(ResponseJson.builder().errorCode(code).errorMessage(e.getMessage()).build(), HttpStatus.OK);
    }




}
