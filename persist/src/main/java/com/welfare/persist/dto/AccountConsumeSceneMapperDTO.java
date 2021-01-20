package com.welfare.persist.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 19:00
 */
@Data
public class AccountConsumeSceneMapperDTO {

  private Long id;
  /**
   * 员工类型ID
   */
  private String accountTypeCode;
  /**
   * 商户代码
   */
  private String merCode;

  /**
   * 员工类型名称
   */
  private String accountTypeName;
  /**
   * 消费配置配置门店
   */
  private List<AccountConsumeStoreRelationDTO> accountConsumeStoreRelationDTOListDTOList;

  /**
   * 使用状态
   */
  private Integer status;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 备注
   */
  private String remark;

  private byte deleted;
}
