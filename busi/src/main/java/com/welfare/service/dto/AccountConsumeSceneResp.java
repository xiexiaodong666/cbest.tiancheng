package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 11:49 上午
 */
@Data
@ApiModel("员工类型消费配置对象")
public class AccountConsumeSceneResp {

  @ApiModelProperty("员工类型消费配置id")
  private String id;

  /**
   * 员工类型ID
   */
  @ApiModelProperty("员工类型Code")
  private String accountTypeCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码为空")
  private String merCode;

  /**
   * 消费配置配置门店
   */
  @ApiModelProperty("消费配置配置门店")
  private List<AccountConsumeSceneSupplierDTO> consumeSceneSupplierDTOS;
}
