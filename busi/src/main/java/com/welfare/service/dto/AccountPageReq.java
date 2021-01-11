package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 14:57
 */
@Data
@ApiModel("员工账户分页参数")
public class AccountPageReq implements Serializable {

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;
  /**
   * 员工姓名
   */
  @ApiModelProperty("员工姓名")
  private  String accountName;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门")
  private String departmentCode;
  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态")
  private Integer accountStatus;
  /**
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  private  String accountTypeCode;
}
