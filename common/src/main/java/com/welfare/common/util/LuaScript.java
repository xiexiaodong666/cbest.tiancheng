package com.welfare.common.util;


import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @Author liyx
 */
public class LuaScript {

    /**
     * 加锁脚本 lock.lua
     * 1. 判断key是否存在
     * 2. 如果存在，判断requestID是否相等
     * 相等，则删除掉key重新创建新的key值，重置过期时间
     * 不相等，说明已经被抢占，加锁失败，返回null
     * 3. 如果不存在，说明恰好已经过期，重新生成key
     */
    public static String LOCK_SCRIPT;

    /**
     * 解锁脚本 unlock.lua
     */
    public static String UN_LOCK_SCRIPT;

    public static void initLockScript() throws IOException {
        if (Strings.isEmpty(LOCK_SCRIPT)) {
            InputStream inputStream = Objects.requireNonNull(
                LuaScript.class.getClassLoader().getResourceAsStream("lock.lua"));
            LOCK_SCRIPT = readFile(inputStream);
        }
    }

    public static void initUnLockScript() throws IOException {
        if (Strings.isEmpty(UN_LOCK_SCRIPT)) {
            InputStream inputStream = Objects.requireNonNull(
                LuaScript.class.getClassLoader().getResourceAsStream("unlock.lua"));
            UN_LOCK_SCRIPT = readFile(inputStream);
        }
    }

    public static RedisScript<Long> getRedisLockScript() {
        return new DefaultRedisScript<>(LOCK_SCRIPT, Long.class);
    }

    private static String readFile(InputStream inputStream) throws IOException {
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line)
                    .append(System.lineSeparator());
            }

            return stringBuilder.toString();
        }
    }

    public static RedisScript<Long> getRedisUnLockScript() {
        return new DefaultRedisScript<>(UN_LOCK_SCRIPT, Long.class);
    }
}
