package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.MerchantSyncDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

import java.util.List;
import java.util.UUID;

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

  @Override
  public Long getSearchKey1() {
    return typeEnum.getEvtType();
  }

  @Override
  public Long getSearchKey2() {
    if(merchantDetailDTOList.size()==1){
      return merchantDetailDTOList.get(0).getId();
    }
    return null;
  }

  @Override
  public UUID getUserToken() {
    return UUID.randomUUID();
  }
}
