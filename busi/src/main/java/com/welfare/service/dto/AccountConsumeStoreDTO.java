package com.welfare.service.dto;

import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 15:02
 */
@Data
public class AccountConsumeStoreDTO {

  private Long accountConsumeSceneId;
  /**
   * 商户代码
   */
  private String merCode;
  /**
   * 门店代码
   */
  private String storeCode;
  /**
   * 门店名称
   */
  private String storeName;
  /**
   * 门店可支持消费方式
   */
  private String consumType;
  /**
   * 员工选择的该门店消费方式
   */
  private String selectConsumType;
}
