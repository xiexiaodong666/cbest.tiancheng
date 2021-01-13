package com.welfare.service.operator.payment.domain;

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
}
