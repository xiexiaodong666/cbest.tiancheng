package com.welfare.service.sync.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.sync.event.AccountConsumeSceneEvt;
import com.welfare.service.sync.event.AccountEvt;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/15 15:25
 */
@Component
@Slf4j
public class AccountConsumeSceneHandler {
  @Autowired
  PersistentBus persistentBus;
  @Autowired
  ShoppingFeignClient shoppingFeignClient;

  private Gson gson= new Gson();


  @PostConstruct
  private void register() {
    try {
      persistentBus.register(this);
    } catch (PersistentBus.EventBusException e) {
      log.error(e.getMessage(), e);
    }
  }
  @AllowConcurrentEvents
  @Subscribe
  public void accountConsumeSceneEvt(AccountConsumeSceneEvt accountConsumeSceneEvt) {
    
  }
}
