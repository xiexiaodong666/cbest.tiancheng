package com.welfare.persist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 17:13
 */
@Data
public class AccountDetailMapperDTO {
  /**
   * 员工名称
   */
  private String accountName;
  /**
   * 员工账号
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private String accountCode;

  /**
   * 账号状态
   */
  private String accountStatus;



  /**
   * 员工类型编码名称
   */
  private String accountTypeName;
  /**
   * 员工类型code
   */
  private String accountTypeCode;
  /**
   * 所属部门
   */
  private String departmentCode;
  /**
   * 所属部门
   */
  private String departmentName;

  /**
   * 商户CODE
   */
  private String merCode;

  /**
   * 商户名称
   */
  private String merName;


  /**
   * 最大授权额度
   */
  private BigDecimal maxQuota;
  /**
   * 剩余授权额度
   */
  private BigDecimal surplusQuota;

  /**
   * 账户余额
   */
  private BigDecimal accountBalance;

  /**
   * 备注
   */
  private String remark;

  private String phone;

  /**
   * 是否激活
   */
  private Integer binding;
}
