package com.welfare.service.dto.nhc;

import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.service.dto.DepartmentTree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:22 上午
 */
@Data
public class NhcAccountInfoDTO {

  @ApiModelProperty("员工名称")
  private String accountName;

  @ApiModelProperty("员工编码")
  private String accountCode;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("组织编码")
  private String departmentCode;

  @ApiModelProperty("组织名称")
  private String departmentName;

  @ApiModelProperty("采购余额")
  private BigDecimal procurementBalance;

  @ApiModelProperty("账号状态(1正常2禁用)")
  private Integer status;


  public static NhcAccountInfoDTO of(Account account, AccountAmountType accountAmountType, DepartmentTree department) {
    BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGUMENTS, "员工不存在");
    BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGUMENTS, "积分福利不存在");
    BizAssert.notNull(department, ExceptionCode.ILLEGALITY_ARGUMENTS, "组织不存在");

    NhcAccountInfoDTO accountInfoDTO = new NhcAccountInfoDTO();
    accountInfoDTO.setAccountName(account.getAccountName());
    accountInfoDTO.setAccountCode(String.valueOf(account.getAccountCode()));
    accountInfoDTO.setPhone(account.getPhone());
    accountInfoDTO.setDepartmentCode(department.getDepartmentCode());
    accountInfoDTO.setDepartmentName(department.getDepartmentName());
    accountInfoDTO.setStatus(account.getAccountStatus());
    accountInfoDTO.setProcurementBalance(accountAmountType.getAccountBalance());
    return accountInfoDTO;
  }
}
