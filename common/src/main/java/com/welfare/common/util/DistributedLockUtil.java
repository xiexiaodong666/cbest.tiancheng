package com.welfare.common.util;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DistributedLockUtil {
    private static RedissonClient redissonClient;

    public static void setRedissonClient(RedissonClient client){
        redissonClient = client;
    }

    public static RLock lockFairly(String key){
        log.info("ready to lock:{}",key);
        RLock lock = lockFairly(key, -1L);
        log.info("locked:{}",key);
        return lock;
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
                    log.info("ready to unlock:{}",lock.getName());
                    lock.unlock();
                    log.info("unlocked:{}",lock.getName());
                }
            });
        }else{
            log.info("ready to unlock:{}",lock.getName());
            lock.unlock();
            log.info("unlocked:{}",lock.getName());
        }
    }
}
