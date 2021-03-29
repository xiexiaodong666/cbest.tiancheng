package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/15 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class AccountEvt implements BusEvent {
  private ShoppingActionTypeEnum typeEnum;
  private List<Account> accountList;
  private List<Long> codeList;

  @Override
  public Long getSearchKey1() {
    return typeEnum.getEvtType();
  }

  @Override
  public Long getSearchKey2() {
    if(!CollectionUtils.isEmpty(accountList)){
      return accountList.get(0).getId();
    }
    if(!CollectionUtils.isEmpty(codeList)){
      return Long.parseLong(String.valueOf(codeList.size()));
    }
    return 0L;
  }

  @Override
  public UUID getUserToken() {
    return UUID.randomUUID();
  }
}
