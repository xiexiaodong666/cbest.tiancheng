package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:37 AM
 */
@Data
@NoArgsConstructor
public class BatchDepositApplyUpdateRequest {

  @ApiModelProperty("请求id（用于幂等处理）")
  private String requestId;

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  private Long id;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;
}