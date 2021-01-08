package com.welfare.persist.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 15:02
 */
@Data
public class AccountConsumeSceneDTO {
  private Long id;
  /**
   * 商户代码
   */
  private String merCode;
  /**
   * 员工类型ID
   */
  private String accountTypeId;
  /**
   * 员工类型名称
   */
  private String accountTypeName;
  /**
   * 消费配置配置门店
   */
  private List<AccountConsumeStoreDTO> accountConsumeStoreDTOList;

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
