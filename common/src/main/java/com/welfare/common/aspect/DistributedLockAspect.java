package com.welfare.common.aspect;

import com.welfare.common.annotation.DistributedLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  Description:
 *
 *  @author Yuxiang Li
 *  @email yuxiang.li@sjgo365.com
 *  @date 1/18/2021
 */
@Aspect
@Component
public class DistributedLockAspect {

    @Autowired
    private RedissonClient redissonClient;


    @Around("@annotation(distributedLock)")
    public void before(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockPrefix = distributedLock.lockPrefix();
        String lockKey = distributedLock.lockKey();

        RLock lock = redissonClient.getLock("");
        try{
            joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
