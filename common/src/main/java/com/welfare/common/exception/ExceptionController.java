package com.welfare.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.result.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice("com.welfare.controller")
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler
    @ResponseBody
    public Result processException(HttpServletRequest req, HttpServletResponse res, Throwable e) {
        ObjectMapper om = new ObjectMapper();
        String reqURL = StringUtils.isNotBlank(req.getQueryString()) ? req.getRequestURL()+"?"+ req.getQueryString()
                : req.getRequestURL().toString();
        String params = null;
        try {
            params = om.writeValueAsString(req.getParameterMap());
        } catch (JsonProcessingException ex) {
            logger.error("参数解析异常：", ex);
            return new Result(ExceptionCode.UNKNOWON_EXCEPTION, null);
        }

        logger.error("业务异常： 请求路径：{}， 业务参数：{}, 异常：", reqURL, params, e);

        if(e instanceof BusiException) {
            return new Result( ((BusiException)e).getCodeEnum(),null);
        }else {
            return new Result(ExceptionCode.UNKNOWON_EXCEPTION, null);
        }
    }
}
