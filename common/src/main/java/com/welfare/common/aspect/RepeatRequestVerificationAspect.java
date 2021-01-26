package com.welfare.common.aspect;

import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.annotation.RepeatRequestVerification;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RepeatRequestVerificationAspect {
  private final RedissonClient redissonClient;
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
