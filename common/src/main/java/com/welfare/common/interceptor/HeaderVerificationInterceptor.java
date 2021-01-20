package com.welfare.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.AccountUser;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.Header;
import com.welfare.common.domain.AccountUserInfo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.domain.UserInfo;
import com.welfare.common.util.AccountUserHolder;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.common.util.UserInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Slf4j
public class HeaderVerificationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
        //
        String source = request.getHeader(WelfareConstant.Header.SOURCE.code());
        if(handler instanceof HandlerMethod){
            setApiUserToContext(handler, request);
            setMerchantUserToContext(handler, request);
            setAccountUserToContext(handler, request);
        }
        if(StringUtils.isEmpty(source)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Source required for http header");
        }
        //todo 需要优化Source校验逻辑
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            MerchantUserHolder.release();
            UserInfoHolder.release();
            AccountUserHolder.release();
        } catch (Exception e) {
            log.error("MerchantUserHolder.release error.", e);
        }
    }

    /**
     * 从header里获取apiUser并存入上下文中
     */
    private void setApiUserToContext(Object handler, HttpServletRequest request) {

        ApiUser apiUser = ((HandlerMethod) handler).getMethodAnnotation(ApiUser.class);
        if (apiUser != null) {
            String apiUserInfo = request.getHeader(WelfareConstant.Header.API_USER.code());
            if(StringUtils.isEmpty(apiUserInfo)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"apiUser required for http header");
            }
            try {
                UserInfoHolder.setApiUserInfoLocal(JSON.parseObject(new String(apiUserInfo.getBytes("ISO-8859-1"),"utf8"), UserInfo.class));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从header里获取merchantUser并存入上下文中
     */
    private void setMerchantUserToContext(Object handler, HttpServletRequest request) {
        MerchantUser merchantUser = ((HandlerMethod) handler).getMethodAnnotation(MerchantUser.class);
        String merchantUserInfo = request.getHeader(WelfareConstant.Header.MERCHANT_USER.code());
        if (merchantUser != null || !StringUtils.isEmpty(merchantUserInfo)) {
            if(merchantUser != null && StringUtils.isEmpty(merchantUserInfo)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"merchantUser required for http header");
            }
            try {
                MerchantUserInfo merchantU = JSON.parseObject(new String(merchantUserInfo.getBytes("ISO-8859-1"),"utf8"), MerchantUserInfo.class);
                if (StringUtils.isEmpty(merchantU.getMerchantCode())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"merchantUser.merchantCode required for http header");
                }
                MerchantUserHolder.setMerchantUser(merchantU);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAccountUserToContext(Object handler, HttpServletRequest request) {
        AccountUser accountUser = ((HandlerMethod) handler).getMethodAnnotation(AccountUser.class);
        String accountUserInfo = request.getHeader(Header.ACCOUNT_USER.code());
        if (accountUser != null || !StringUtils.isEmpty(accountUserInfo)) {
            if(accountUser != null && StringUtils.isEmpty(accountUserInfo)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"accountUser required for http header");
            }
            try {
                AccountUserHolder.setAccountUser(JSON.parseObject(new String(accountUserInfo.getBytes("ISO-8859-1"),"utf8"), AccountUserInfo.class));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
