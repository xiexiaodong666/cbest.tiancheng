package com.welfare.service.sync.config;

import org.killbill.bus.DefaultPersistentBus;
import org.killbill.bus.api.PersistentBus;
import org.killbill.bus.api.PersistentBusConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author joewee
 * @version 1.0.0
 * @date 2020/8/5 13:11
 */
@Configuration
public class DefaultPersistentBusConfig {

  /**
   * @see PersistentBusConfig
   */
  @Bean(initMethod = "startQueue", destroyMethod = "stopQueue")

  public PersistentBus defaultPersistentBus(DataSource dataSource) {
    final Properties properties = new Properties();
    properties.setProperty("org.killbill.persistent.bus.main.inMemory", "false");
    properties.setProperty("org.killbill.persistent.bus.main.queue.mode", "STICKY_POLLING");
    properties.setProperty("org.killbill.persistent.bus.main.max.failure.retry", "10");
    //Number of bus events to fetch from the database at once (only valid in 'polling mode')
    properties.setProperty("org.killbill.persistent.bus.main.claimed", "500");
    properties.setProperty("org.killbill.persistent.bus.main.claim.time", "5m");
    //查询数据库的时间间隔，单位毫秒，默认3000ms
    properties.setProperty("org.killbill.persistent.bus.main.sleep", "3000");
    properties.setProperty("org.killbill.persistent.bus.main.off", "false");
    //Max number of dispatch threads to use
    properties.setProperty("org.killbill.persistent.bus.main.nbThreads", "10");
    //Size of the inflight queue (only valid in STICKY_EVENTS mode)
    properties.setProperty("org.killbill.persistent.bus.main.queue.capacity", "10000");
    //未被事件拥有者执行的时间即需被重新分发的时间间隔默认10m
    properties.setProperty("org.killbill.persistent.bus.main.reapThreshold", "5m");
    //被重新分发的单次数量
    properties.setProperty("org.killbill.persistent.bus.main.maxReDispatchCount", "500");
    //触发收割线程的周期
    properties.setProperty("org.killbill.persistent.bus.main.reapSchedule", "1m");
    properties.setProperty("org.killbill.persistent.bus.main.tableName", "bus_events");
    properties
        .setProperty("org.killbill.persistent.bus.main.historyTableName", "bus_events_history");
    return new DefaultPersistentBus(dataSource, properties);
  }

}
