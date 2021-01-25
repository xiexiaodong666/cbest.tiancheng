package com.welfare.service.operator.payment.domain;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantAccountType;
import lombok.Data;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;

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
        Assert.notNull(accountAmountType,"子账户不能为空");
        Assert.notNull(merchantAccountType,"商户子账户不能为空");
        accountAmountDO.setAccountAmountType(accountAmountType);
        accountAmountDO.setMerchantAccountType(merchantAccountType);
        accountAmountDO.setAccount(account);
        return accountAmountDO;
    }

    public static BigDecimal calculateAccountCredit(List<AccountAmountType> accountTypes) {
        return accountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateAccountBalance(List<AccountAmountType> accountTypes) {
        return accountTypes.stream()
                .filter(accountAmountType -> !SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
