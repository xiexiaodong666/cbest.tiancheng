package com.welfare.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(value = {"com.welfare.controller","com.welfare.serviceaccount.controller","com.welfare.servicemerchant.controller","com.welfare.servicesettlement.controller"})
public class ExceptionController implements IController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler
    @ResponseBody
    public R processException(HttpServletRequest req, HttpServletResponse res, Throwable e) {
        ObjectMapper om = new ObjectMapper();
        String reqURL = StringUtils.isNotBlank(req.getQueryString()) ? req.getRequestURL()+"?"+ req.getQueryString()
                : req.getRequestURL().toString();
        String params = null;
        try {
            params = om.writeValueAsString(req.getParameterMap());
        } catch (JsonProcessingException ex) {
            logger.error("参数解析异常：", ex);
            return fail(SystemCode.PARAM_BIND_ERROR,ex.getMessage());
        }
        logger.error("业务异常： 请求路径：{}， 业务参数：{}, 异常：", reqURL, params, e);

        if(e instanceof BizException){
            return fail(((BizException) e).getCode());
        }else{
            return fail(SystemCode.FAILURE,e.getMessage());
        }

    }
}
