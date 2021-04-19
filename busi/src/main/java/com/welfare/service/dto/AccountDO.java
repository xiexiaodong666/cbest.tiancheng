package com.welfare.service.dto;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/4/2021
 */
@Data
public class AccountDO {
    @ApiModelProperty("账户编码")
    private Long accountCode;
    @ApiModelProperty("账户名称")
    private String accountName;
    @ApiModelProperty("账户类型编码")
    private String accountTypeCode;
    @ApiModelProperty("账户类型名称")
    private String accountTypeName;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("商户名称")
    private String merName;
    @ApiModelProperty("部门编码")
    private String departmentCode;
    @ApiModelProperty("部门名称")
    private String departmentName;
    @ApiModelProperty("离线锁定标识")
    private Integer offlineLock;

    public static AccountDO of(Account account, Department department, Merchant merchant){
        AccountDO accountDO = new AccountDO();
        accountDO.setAccountCode(account.getAccountCode());
        accountDO.setAccountName(account.getAccountName());
        accountDO.setAccountTypeCode(account.getAccountTypeCode());
        accountDO.setPhone(account.getPhone());
        accountDO.setDepartmentName(department.getDepartmentName());
        accountDO.setDepartmentCode(department.getDepartmentCode());
        accountDO.setMerCode(merchant.getMerCode());
        accountDO.setMerName(merchant.getMerName());
        accountDO.setOfflineLock(account.getOfflineLock());
        return accountDO;
    }
}
