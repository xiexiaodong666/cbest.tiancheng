package com.welfare.common.aspect;

import com.welfare.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

/**
 *  Description:
 *
 *  @author Yuxiang Li
 *  @email yuxiang.li@sjgo365.com
 *  @date 1/18/2021
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object before(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockPrefix = distributedLock.lockPrefix();
        String lockKey = getLockKey(joinPoint, distributedLock);
        RLock lock = redissonClient.getLock(lockPrefix + lockKey);
        lock.lock();
        try{
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String lockKey = distributedLock.lockKey();
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(lockKey,new TemplateParserContext());
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(),args[i]);
            context.setVariable(String.valueOf(i),args[i]);
        }
        String realKey = expression.getValue(context,String.class);
        return realKey;
    }
}
