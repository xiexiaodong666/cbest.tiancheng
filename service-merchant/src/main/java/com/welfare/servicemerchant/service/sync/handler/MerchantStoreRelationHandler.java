package com.welfare.servicemerchant.service.sync.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.exception.BusiException;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionReq;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.sync.event.MerchantStoreRelationEvt;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2020/12/29 10:30
 */
@Component
@Slf4j
public class MerchantStoreRelationHandler {

  @Autowired
  PersistentBus persistentBus;
  @Autowired(required = false)
  private ShoppingFeignClient shoppingFeignClient;

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
  public void onMerchantStoreRelationChange(MerchantStoreRelationEvt evt) {

    RoleConsumptionReq roleConsumptionReq = evt.getRoleConsumptionReq();
    log.info("消费门店同步请求数据:{}", roleConsumptionReq);
    RoleConsumptionResp resp = shoppingFeignClient.addOrUpdateRoleConsumption(roleConsumptionReq);
    log.info("消费门店同步返回数据:{}", resp);

    if (!("0000").equals(resp.getCode())) {
      throw new BusiException("同步门店数据到商城中心失败msg【" + resp.getMsg() + "】");

    }
  }
}
