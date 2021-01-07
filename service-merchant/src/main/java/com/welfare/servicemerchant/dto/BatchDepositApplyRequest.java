package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  5:18 PM
 */
@Data
@NoArgsConstructor
@ApiModel("批量导入账号申请请求")
public class BatchDepositApplyRequest {

  /**
   * 请求id（用于幂等处理）
   */
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


  @ApiModelProperty("申请员工额度信息")
  private List<AccountDepositRequest> items;
}