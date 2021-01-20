package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 15:31
 */
@Data
@ApiModel("员工消费场景配置")
public class AccountConsumeSceneResp implements Serializable {

  /**
   * 员工类型编号
   */
  @ApiModelProperty("员工类型编号")
  private String accountTypeCode;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;
  /**
   * 消费配置配置门店
   */
  @ApiModelProperty("配置门店")
  private List<AccountConsumeStoreResp> accountConsumeStoreRespList;

  /**
   * 使用状态
   */
  @ApiModelProperty("使用状态")
  private Integer status;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
}
