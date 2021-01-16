package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.Account;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

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

  @Override
  public Long getSearchKey1() {
    return typeEnum.getEvtType();
  }

  @Override
  public Long getSearchKey2() {
    if(accountList.size()==1){
      return accountList.get(0).getId();
    }
    return null;
  }

  @Override
  public UUID getUserToken() {
    return UUID.randomUUID();
  }
}
