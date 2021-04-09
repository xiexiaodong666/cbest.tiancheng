package com.welfare.persist.dto;

import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 5:42 下午
 */
@Data
public class PayChannelMerchantDTO {

  private String merchantCode;

  private String merchantName;

  private String paymentChannels;
}
