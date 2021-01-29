package com.welfare.common.perf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */

public class PerfMonitor {
    private Logger log = LoggerFactory.getLogger(PerfMonitor.class);
    private AtomicLong time = new AtomicLong();
    private AtomicInteger count = new AtomicInteger();
    private AtomicLong lastLogTime = new AtomicLong();
    private ThreadLocal<Long> local = new ThreadLocal<Long>();

    private final long interval;

    private final String name;

    public PerfMonitor(String name, long interval) {
        super();
        this.name = name;
        this.interval = interval;
    }

    public PerfMonitor(String name) {
        super();
        this.name = name;
        this.interval = 20000;
    }

    public void start() {
        local.set(System.currentTimeMillis());
    }

    public void stop() {
        count.incrementAndGet();
        time.addAndGet((System.currentTimeMillis() - local.get()));
        log();
    }

    private void log() {
        // 20秒一次
        long now = System.currentTimeMillis();
        long last = lastLogTime.get();
        if (now > last + interval) {
            synchronized (this) {
                lastLogTime.compareAndSet(last, now);
                long c = count.get()==0?1:count.get();
                long t = time.get();
                log.error(MessageFormat.format("name:{0}，count:{1}，rt:{2}", name, c, (t / c)));
                time.set(0);
                count.set(0);
            }
        }
    }
}
