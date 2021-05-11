package com.welfare.service.dto;

import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 账号余额申请请求类
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  2:14 PM
 */
@Data
@ApiModel("账号余额申请")
@NoArgsConstructor
public class AccountDepositRequest {

  /**
   * 员工账号
   */
  @ApiModelProperty(name = "员工(手机号)账号", required = true)
  private String phone;

  /**
   * 申请充值总额
   */
  @ApiModelProperty(value = "申请充值金额", required = true)
  @NotNull(message = "申请充值金额不能为空")
  @DecimalMax(message = "金额超过限制[99999999.99]", value = "99999999.99")
  private BigDecimal rechargeAmount;

  public static List<AccountDepositRequest> of(List<TempAccountDepositApplyDTO> deposits) {
    List<AccountDepositRequest> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(deposits)) {
      deposits.forEach(tempAccountDepositApplyDTO -> {
        AccountDepositRequest depositRequest = new AccountDepositRequest();
        depositRequest.setPhone(tempAccountDepositApplyDTO.getPhone());
        depositRequest.setRechargeAmount(tempAccountDepositApplyDTO.getRechargeAmount());
        list.add(depositRequest);
      });
    }
    return list;
  }
}