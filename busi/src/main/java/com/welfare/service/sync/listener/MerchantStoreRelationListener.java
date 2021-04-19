package com.welfare.service.sync.listener;


import com.welfare.service.sync.event.MerchantStoreRelationEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.killbill.bus.api.PersistentBus.EventBusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2020/8/5 13:16
 */
@Component
@Slf4j
public class MerchantStoreRelationListener {

  @Autowired(required = false)
  PersistentBus persistentBus;

  @Autowired
  DataSource dataSource;


  @EventListener
  @Transactional(rollbackFor = Exception.class)
  public void onMerchantStoreRelationChange(MerchantStoreRelationEvt evt) throws EventBusException {
    log.info("监听器。。。。{}", "");

    persistentBus.postFromTransaction(evt, DataSourceUtils.getConnection(dataSource));
  }
}
