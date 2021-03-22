package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.entity.MessagePushConfigContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

import java.util.List;
import java.util.UUID;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 5:07 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MessagePushConfigEvt implements BusEvent {

  private MessagePushConfigContact contact;

  @Override
  public Long getSearchKey1() {
    return contact.getId();
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
