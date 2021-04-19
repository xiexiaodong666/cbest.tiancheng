package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 1:15 下午
 */
@Data
public class AccountConsumeSceneEditReq {

  /**
   * 场景ID
   */
  @ApiModelProperty("id")
  private String id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码为空")
  private String merCode;

  /**
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  @NotEmpty(message = "员工类型编码为空")
  private String accountTypeCode;

  /**
   * 员工类型编码
   */
  @ApiModelProperty("对应消费门店配置")
  private List<AccountConsumeStoreRelationEditReq> accountConsumeStoreRelationEditReqs;
}
