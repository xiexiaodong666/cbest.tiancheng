package com.welfare.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String source = request.getHeader(WelfareConstant.Header.SOURCE.code());
        setMerchantUserToContext(handler, request);
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
        } catch (Exception e) {
            log.error("MerchantUserHolder.release error.", e);
        }
    }

    /**
     * 从header里获取merchantUser并存入上下文中
     */
    private void setMerchantUserToContext(Object handler, HttpServletRequest request) {
        MerchantUser merchantUser = ((HandlerMethod) handler).getMethodAnnotation(MerchantUser.class);
        if (merchantUser != null) {
            String merchantUserInfo = request.getHeader(WelfareConstant.Header.MERCHANT_USER.code());
            if(StringUtils.isEmpty(merchantUserInfo)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"merchantUser required for http header");
            }
            MerchantUserHolder.setDeptIds(JSON.parseObject(merchantUserInfo, MerchantUserInfo.class));
        }
    }
}
