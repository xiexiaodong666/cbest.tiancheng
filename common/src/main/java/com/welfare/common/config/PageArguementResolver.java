package com.welfare.common.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageArguementResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class);
    }

    @Override
    public PageRequest resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String pageStr = webRequest.getParameter("page");
        String sizeStr = webRequest.getParameter("size");
        PageRequest request = PageRequest.of(StringUtils.isNotBlank(pageStr)? Integer.valueOf(pageStr) : 0,
                StringUtils.isNotBlank(sizeStr)? Integer.valueOf(sizeStr):100);
        return request;
    }
}
