package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.service.remote.entity.RoleConsumptionReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

import java.util.UUID;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/14 5:54 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class MerchantStoreRelationEvt implements BusEvent {

  private RoleConsumptionReq roleConsumptionReq;

  @Override
  public Long getSearchKey1() {
    return null;
  }

  @Override
  public Long getSearchKey2() {
    return null;
  }

  @Override
  public UUID getUserToken() {
    return null;
  }
}
