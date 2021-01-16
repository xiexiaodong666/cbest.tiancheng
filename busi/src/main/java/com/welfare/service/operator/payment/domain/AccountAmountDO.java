package com.welfare.service.operator.payment.domain;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantAccountType;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/13/2021
 */
@Data
public class AccountAmountDO {
    private AccountAmountType accountAmountType;
    private MerchantAccountType merchantAccountType;
    private Account account;
    private String transNo;
    public static AccountAmountDO of(AccountAmountType accountAmountType, MerchantAccountType merchantAccountType, Account account){
        AccountAmountDO accountAmountDO = new AccountAmountDO();
        accountAmountDO.setAccountAmountType(accountAmountType);
        accountAmountDO.setMerchantAccountType(merchantAccountType);
        accountAmountDO.setAccount(account);
        return accountAmountDO;
    }
}
