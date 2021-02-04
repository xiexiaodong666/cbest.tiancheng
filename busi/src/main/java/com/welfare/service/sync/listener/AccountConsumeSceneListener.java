package com.welfare.service.sync.listener;

import com.welfare.service.sync.event.AccountConsumeSceneEvt;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.killbill.bus.api.PersistentBus.EventBusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/15 15:25
 */
@Component
@Slf4j
public class AccountConsumeSceneListener {

  @Autowired
  PersistentBus persistentBus;

  @Autowired
  DataSource dataSource;

  @EventListener
  @Transactional(rollbackFor = Exception.class)
  public void onAdd(AccountConsumeSceneEvt evt) throws EventBusException {
    persistentBus.postFromTransaction(evt, DataSourceUtils.getConnection(dataSource));
  }
}
