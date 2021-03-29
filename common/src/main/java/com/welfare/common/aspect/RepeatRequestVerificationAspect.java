package com.welfare.common.aspect;

import com.welfare.common.annotation.RepeatRequestVerification;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RepeatRequestVerificationAspect {
  private final StringRedisTemplate stringRedisTemplate;

  @Around("@annotation(repeatRequestVerification)")
  public Object before(ProceedingJoinPoint joinPoint, RepeatRequestVerification repeatRequestVerification) throws Throwable {
    String lockPrefix = repeatRequestVerification.prefixKey();
    String key = JsonUtil.toJson(joinPoint.getArgs());
    if(stringRedisTemplate.opsForValue().setIfAbsent(lockPrefix +key ,"true",1, TimeUnit.SECONDS)){
      return joinPoint.proceed();
    }else{
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"请勿重复提交",null);
    }
  }

}
