package com.welfare.service.sync.listener;

import com.welfare.service.sync.event.MessagePushConfigEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 5:09 下午
 */
@Component
@Slf4j
public class MessagePushConfigListener {

  @Autowired(required = false)
  PersistentBus persistentBus;

  @Autowired
  DataSource dataSource;


  @EventListener
  @Transactional(rollbackFor = Exception.class)
  public void onAdd(MessagePushConfigEvt evt) throws PersistentBus.EventBusException {
    persistentBus.postFromTransaction(evt, DataSourceUtils.getConnection(dataSource));
  }
}
