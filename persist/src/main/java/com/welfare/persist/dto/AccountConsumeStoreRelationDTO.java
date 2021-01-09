package com.welfare.persist.dto;

import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 19:00
 */
@Data
public class AccountConsumeStoreRelationDTO {


  private Long id;
  /**
   * 门店编码
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
  private String sceneConsumType;

}
