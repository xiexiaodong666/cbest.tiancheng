package com.welfare.common.annotation;

import java.lang.annotation.*;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/18/2021
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    /**
     * 分布式锁的key前缀
     * @return
     */
    String lockPrefix();

    /**
     * 分布式锁的key
     * @return
     */
    String lockKey();
}
