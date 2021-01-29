package com.welfare.servicemerchant.dto;

import java.util.Set;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/28 5:15 PM
 */
@Data
public class DisableCardDTO {
  private Set<String> cardIdSet;

  private Integer enabled;
}
