package com.welfare.common.util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author liyx
 * @date 2020/8/6
 * @time 21:13
 * @description:
 */
@Slf4j
public class RedisDistributeLock {

    public static RedisTemplate redisTemplate;

    /**
     * 持有锁 成功标识
     * */
    private static final Long ADD_LOCK_SUCCESS = 1L;
    /**
     * 释放锁 失败标识
     * */
    private static final Long RELEASE_LOCK_SUCCESS = 1L;

    /**
     * 默认过期时间 单位：秒
     * */
    private static final int DEFAULT_EXPIRE_TIME_SECOND = 1;
    /**
     * 默认加锁重试时间 单位：毫秒
     * */
    private static final int DEFAULT_RETRY_FIXED_TIME = 100;
    /**
     * 默认的加锁浮动时间区间 单位：毫秒
     * */
    private static final int DEFAULT_RETRY_TIME_RANGE = 10;
    /**
     * 默认的加锁重试次数
     * */
    private static final int DEFAULT_RETRY_COUNT = 3;

    @Setter
    private static int defaultExpireTimeSecond = DEFAULT_EXPIRE_TIME_SECOND;
    @Setter
    private static int defaultRetryFixedTime = DEFAULT_RETRY_FIXED_TIME;
    @Setter
    private static int defaultRetryTimeRange = DEFAULT_RETRY_TIME_RANGE;
    @Setter
    private static int defaultRetryCount = DEFAULT_RETRY_COUNT;

    public RedisDistributeLock(RedisTemplate redisTemplate) {
        try {
            RedisDistributeLock.redisTemplate = redisTemplate;
            LuaScript.initLockScript();
            LuaScript.initUnLockScript();
        } catch (IOException e) {
            throw new RuntimeException("LuaScript init error!",e);
        }
    }

    public static String lock(String lockKey) {
        String uuid = UUID.randomUUID().toString();

        return lock(lockKey,uuid);
    }

    public static String lock(String lockKey, int expireTime) {
        String uuid = UUID.randomUUID().toString();

        return lock(lockKey,uuid,expireTime);
    }

    public static String lock(String lockKey, String requestID) {
        return lock(lockKey,requestID,defaultExpireTimeSecond);
    }

    public static String lock(String lockKey, String requestID, int expireTime) {
        List<String> keyList = Collections.singletonList(lockKey);

        List<String> argsList = Arrays.asList(
                requestID,
                expireTime + ""
        );

        Long result = (Long) redisTemplate.execute(LuaScript.getRedisLockScript(), keyList, argsList);

        if(result.equals(ADD_LOCK_SUCCESS)){
            return requestID;
        }else{
            return null;
        }
    }

    public static String lockAndRetry(String lockKey) {
        String uuid = UUID.randomUUID().toString();
        return lockAndRetry(lockKey, uuid);
    }

    public static String lockAndRetry(String lockKey, String requestID) {
        return lockAndRetry(lockKey, requestID, defaultExpireTimeSecond);
    }

    public static String lockAndRetry(String lockKey, String requestID, int expireTime) {
        return lockAndRetry(lockKey, requestID, expireTime, DEFAULT_RETRY_COUNT);
    }

    public static String lockAndRetry(String lockKey, String requestID, int expireTime, int retryCount) {
        if(retryCount <= 0){
            // retryCount小于等于0 无限循环，一直尝试加锁
            while(true){
                String result = lock(lockKey,requestID,expireTime);
                if(result != null){
                    return result;
                }

                log.info("加锁失败，稍后重试：lockKey={},requestID={}",lockKey,requestID);
                // 休眠一会
                sleepSomeTime();
            }
        }else{
            // retryCount大于0 尝试指定次数后，退出
            for(int i=0; i<retryCount; i++){
                String result = lock(lockKey,requestID,expireTime);
                if(result != null){
                    return result;
                }

                // 休眠一会
                sleepSomeTime();
            }

            return null;
        }
    }



    /**
     * 获得最终的获得锁的重试时间
     * */
    private static int getFinallyGetLockRetryTime(){
        Random ra = new Random();

        // 最终重试时间 = 固定时间 + 浮动时间
        return defaultRetryFixedTime + ra.nextInt(defaultRetryTimeRange);
    }

    /**
     * 当前线程 休眠一端时间
     * */
    private static void sleepSomeTime(){
        // 重试时间 单位：毫秒
        int retryTime = getFinallyGetLockRetryTime();
        try {
            Thread.sleep(retryTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("redis锁重试时，出现异常",e);
        }
    }

    public static boolean unLock(String lockKey, String requestID) {
        List<String> keyList = Collections.singletonList(lockKey);

        List<String> argsList = Collections.singletonList(requestID);

        Object result = (Long) redisTemplate.execute(LuaScript.getRedisUnLockScript(), keyList, argsList);

        // 释放锁成功
        return RELEASE_LOCK_SUCCESS.equals(result);
    }

}
