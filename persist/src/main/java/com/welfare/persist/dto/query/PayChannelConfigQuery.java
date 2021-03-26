package com.welfare.persist.dto.query;

import lombok.Data;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 7:33 下午
 */
@Data
public class PayChannelConfigQuery {

  private String merchantCode;

  private String supplierCode;

  private List<String> consumeTypes;

  private List<String> paymentChannelCode;

  private String storeName;
}
