package com.welfare.common.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.welfare.common.util.RequestUtil;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.apache.commons.lang.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jooq.lambda.Seq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Aspect
public class SignatureAspect {
    @Pointcut("@annotation(com.welfare.common.annotation.Signature)")
    private void aspect(){}

    public void before(JoinPoint jp){
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        int requestBodyIndex = -1;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                requestBodyIndex = i;
                break;
            }
        }
        Object requestBody = jp.getArgs()[requestBodyIndex];
        Map<String, Object> map = BeanUtil.toMap(requestBody);
        Map<String, String> lowerCaseKeyMap = new HashMap<>();
        map.entrySet().forEach(e -> lowerCaseKeyMap.put(
                e.getKey().toLowerCase(),
                e.getValue()==null?"":e.getValue().toString()));

        HttpServletRequest request = RequestUtil.currentRequest();
        String signature = request.getHeader("Signature");
        String signatureFields = request.getHeader("SignatureFields");
        String[] fields = signatureFields.split(",");
        StringBuilder builder = new StringBuilder();
        for (String field : fields) {
            builder.append(lowerCaseKeyMap.get(field.toLowerCase()));
        }




    }
}
