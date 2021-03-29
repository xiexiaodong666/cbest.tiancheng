package com.welfare.persist.dto;

import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 7:11 下午
 */
@Data
public class PayChannelConfigSimple {

  private String merchantId;

  private String merchantCode;

  private String merchantName;

  private Integer consumeStoreCount;

  private String paymentChannels;
}
