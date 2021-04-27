package com.welfare.service.dto.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.welfare.persist.dto.AccountSimpleInfo;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/14/2021
 */
@Data
public class AccountAmountTypeGroupDTO {
    @ApiModelProperty("组内账户列表")
    private List<AccountSimpleInfo> accounts;
    /**
     * 主键
     */
    @ApiModelProperty("主键")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long id;
    /**
     * 账户组编码
     */
    @ApiModelProperty("账户组编码")
    private String groupCode;
    /**
     * 福利类型
     */
    @ApiModelProperty("福利类型")
    private String merAccountTypeCode;
    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private BigDecimal balance;

    public static AccountAmountTypeGroupDTO of(AccountAmountTypeGroup accountAmountTypeGroup, List<Account> accounts){
        AccountAmountTypeGroupDTO accountAmountTypeGroupDTO = new AccountAmountTypeGroupDTO();
        accountAmountTypeGroupDTO.setGroupCode(accountAmountTypeGroupDTO.getGroupCode());
        accountAmountTypeGroupDTO.setMerAccountTypeCode(accountAmountTypeGroup.getMerAccountTypeCode());
        accountAmountTypeGroupDTO.setBalance(accountAmountTypeGroup.getBalance());
        List<AccountSimpleInfo> accountSimpleInfos = accounts.stream().map(AccountSimpleInfo::of).collect(Collectors.toList());
        accountAmountTypeGroupDTO.setAccounts(accountSimpleInfos);
        return accountAmountTypeGroupDTO;
    }
}
