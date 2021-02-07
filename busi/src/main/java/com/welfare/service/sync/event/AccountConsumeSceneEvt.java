package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
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
 * @date 2021/1/15 15:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class AccountConsumeSceneEvt implements BusEvent {
  private ShoppingActionTypeEnum typeEnum;
  private List<AccountConsumeSceneStoreRelation> relationList;

  @Override
  public Long getSearchKey1() {
    return typeEnum.getEvtType();
  }

  @Override
  public Long getSearchKey2() {
    return null;
  }

  @Override
  public UUID getUserToken() {
    return UUID.randomUUID();
  }
}
