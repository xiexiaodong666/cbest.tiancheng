package com.welfare.persist.dto;

import com.welfare.persist.entity.Account;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/14/2021
 */
@Data
public class AccountSimpleInfo {
    @ApiModelProperty("账户编码")
    private Long accountCode;
    @ApiModelProperty("账户名称")
    private String accountName;
    @ApiModelProperty("电话")
    private String phone;

    public static AccountSimpleInfo of(Account account){
        AccountSimpleInfo accountSimpleInfo = new AccountSimpleInfo();
        accountSimpleInfo.setAccountName(account.getAccountName());
        accountSimpleInfo.setAccountCode(account.getAccountCode());
        accountSimpleInfo.setPhone(account.getPhone());
        return accountSimpleInfo;
    }

}
