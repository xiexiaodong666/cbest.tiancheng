package com.welfare.servicemerchant.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.util.EmptyChecker;
import com.welfare.service.dto.MerchantSyncDTO;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2019-11-03 21:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class MerchantEvt implements BusEvent {
  private ShoppingActionTypeEnum typeEnum;
  private List<MerchantSyncDTO> merchantDetailDTOList;
  //业务发生时间
  protected Date timestamp;
  @Override
  public Long getSearchKey1() {
    return typeEnum.getEvtType();
  }

  @Override
  public Long getSearchKey2() {
    if(EmptyChecker.notEmpty(merchantDetailDTOList)
            &&merchantDetailDTOList.size()==1){
      return merchantDetailDTOList.get(0).getId();
    }
    return null;
  }

  @Override
  public UUID getUserToken() {
    return UUID.randomUUID();
  }
}
