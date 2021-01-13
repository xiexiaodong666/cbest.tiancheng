package com.welfare.service.operator.payment.domain;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.operator.payment.AbstractPaymentOperator;
import lombok.AllArgsConstructor;
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
    private String transNo;
    public static AccountAmountDO of(AccountAmountType accountAmountType, MerchantAccountType merchantAccountType){
        AccountAmountDO accountAmountDO = new AccountAmountDO();
        accountAmountDO.setAccountAmountType(accountAmountType);
        accountAmountDO.setMerchantAccountType(merchantAccountType);
        return accountAmountDO;
    }
}
