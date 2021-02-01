package com.welfare.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 17:25
 */
@Data
@ApiModel("新增修改入参")
public class AccountReq implements Serializable {
  /**
   * id
   */
  @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  @NotEmpty(message = "员工名称为空")
  private String accountName;

  /**
   * 商户CODE
   */
  @ApiModelProperty("商户CODE")
  @NotEmpty(message = "商户CODE为空")
  private String merCode;
  /**
   * 员工账号
   */
  @ApiModelProperty("员工手机号码")
  @NotEmpty(message = "员工手机号码为空")
  private String phone;

  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态")
  @NotNull(message = "账号状态为空")
  private Integer accountStatus;

  /**
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码")
  @NotEmpty(message = "员工类型编码为空")
  private String accountTypeCode;

  /**
   * 是否授信
   */
  @ApiModelProperty("是否授信,1是 0否")
  private Boolean credit;

  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门Code")
  @NotEmpty(message = "所属部门为空")
  private String departmentCode;


  /**
   * 最大授权额度
   */
  @ApiModelProperty("最大授权额度")
  private BigDecimal maxQuota;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;

  /**
   * 照片url
   */
  @ApiModelProperty("照片url")
  private String imgUrl;
}
