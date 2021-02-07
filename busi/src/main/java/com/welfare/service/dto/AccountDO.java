package com.welfare.service.dto;

import com.welfare.persist.entity.Account;
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
@ApiModel("账户信息")
public class AccountDO {
    @ApiModelProperty("账户编码")
    private Long accountCode;
    @ApiModelProperty("账户名称")
    private String accountName;
    @ApiModelProperty("账户类型编码")
    private String accountTypeCode;
    @ApiModelProperty("账户类型名称")
    private String accountTypeName;

    public static AccountDO of(Account account){
        AccountDO accountDO = new AccountDO();
        accountDO.setAccountCode(account.getAccountCode());
        accountDO.setAccountName(account.getAccountName());
        accountDO.setAccountTypeCode(account.getAccountTypeCode());
        return accountDO;
    }
}
