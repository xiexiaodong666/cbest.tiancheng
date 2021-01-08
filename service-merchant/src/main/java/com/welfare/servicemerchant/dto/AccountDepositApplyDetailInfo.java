package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 账号额度申请详情
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  4:22 PM
 */
@Data
@NoArgsConstructor
@ApiModel("充值申请详情")
public class AccountDepositApplyDetailInfo {

  /**
   * 充值申请主数据
   */
  @ApiModelProperty("充值申请主数据")
  private AccountDepositApplyInfo mainInfo;

  @ApiModelProperty("充值员工")
  private List<AccountDepositApplyItem> items;
}