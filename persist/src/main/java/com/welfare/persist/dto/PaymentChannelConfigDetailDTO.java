package com.welfare.persist.dto;

import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 8:38 下午
 */
@Data
public class PaymentChannelConfigDetailDTO {

  private String merchantCode;

  private String merchantName;

  private String storeName;

  private String storeCode;

  /**
   * 商户的门店消费场景配置 json
   */
  private String merConsumType;

  private String consumeType;

  private String paymentChannelConfigId;

  private String paymentChannelCode;

  private String paymentChannelName;

  private String showOrder;
}
