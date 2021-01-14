package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class MerchantAddEvt implements BusEvent {
  String test;

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
