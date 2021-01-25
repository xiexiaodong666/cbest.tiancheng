package com.welfare.common.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/25/2021
 */
public class DistributedLockUtil {
    private static RedissonClient redissonClient;

    public static void setRedissonClient(RedissonClient client){
        redissonClient = client;
    }

    public static RLock lockFairly(String key){
        return lockFairly(key,-1L);
    }

    public static RLock lockFairly(String key,Long expireSecs){
        RLock lock = redissonClient.getFairLock(key);
        lock.lock(expireSecs, TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lockUnfairly(String key,Long expireSecs){
        RLock lock = redissonClient.getLock(key);
        lock.lock(expireSecs,TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lockUnfairly(String key){
        return lockUnfairly(key,-1L);
    }


    public static void unlock(RLock lock){
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    lock.unlock();
                }
            });
        }else{
            lock.unlock();
        }
    }
}
