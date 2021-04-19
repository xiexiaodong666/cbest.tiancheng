package com.welfare.servicemerchant.dto;

import lombok.Data;

import java.util.Set;

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
