package com.welfare.service.sync.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.MerchantService;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.EmployerDTO;
import com.welfare.service.remote.entity.EmployerReqDTO;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/15 14:07
 */

@Component
@Slf4j
public class AccountHandler {

  @Autowired
  PersistentBus persistentBus;
  @Autowired
  ShoppingFeignClient shoppingFeignClient;
  @Autowired
  private MerchantService merchantService;

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
  public void accountEvt(AccountEvt accountEvt) {
    ShoppingActionTypeEnum actionTypeEnum = accountEvt.getTypeEnum();
    List<Account> accountList = accountEvt.getAccountList();
    List<String> merchantCodeList = accountList.stream().map(account -> {
      return account.getMerCode();
    }).collect(Collectors.toList());
    List<Merchant> merchantList = merchantService.queryMerchantByCodeList(merchantCodeList);
    Map<String, Merchant> merchantMap = new HashMap<String, Merchant>();
    merchantList.forEach(merchant -> {
      merchantMap.put(merchant.getMerCode(), merchant);
    });

    List<AccountSyncDTO> accountSyncDTOList = AccountUtils.getSyncDTO(accountList, merchantMap);
    // 员工账号数据同步
    List<EmployerDTO> employerDTOList = AccountUtils.assemableEmployerDTOList(accountSyncDTOList);
    EmployerReqDTO employerReqDTO = new EmployerReqDTO();
    employerReqDTO.setActionType(actionTypeEnum);
    employerReqDTO.setRequestId(UUID.randomUUID().toString());
    employerReqDTO.setTimestamp(String.valueOf(new Date().getTime()));
    employerReqDTO.setList(employerDTOList);

    log.info("批量添加、修改员工账号 addOrUpdateEmployer:{}",
        gson.toJson(employerReqDTO));
    RoleConsumptionResp roleConsumptionResp = shoppingFeignClient
        .addOrUpdateEmployer(employerReqDTO);
    if (!("0000").equals(roleConsumptionResp.getCode())) {
      throw new BusiException("同步员工账户数据到商城中心失败msg【" + roleConsumptionResp.getMsg() + "】");
    }

  }
}
