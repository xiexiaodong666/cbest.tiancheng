package com.welfare.persist.dto;

import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
public class MerchantStoreRelationDTO {

  /**
   * id
   */
  private Long id;

  /**
   * 商户代码
   */
  private String merCode;

  /**
   * 商户名称
   */
  private String merName;

  /**
   * 门店编码
   */
  private String storeCode;

  /**
   * 门店名称
   */
  private String storeAlias;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 备注
   */
  private String ramark;

  /**
   * 状态
   */
  private String status;

}
