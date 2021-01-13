package com.welfare.service.operator.payment.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantAccountType;
import lombok.Data;
import org.springframework.core.Ordered;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/13/2021
 */
@Data
public class PaymentOperation {
    private MerchantAccountType merchantAccountType;
    private AccountAmountType accountAmountType;

    /**
     * 商户编码
     */
    private String merCode;
    /**
     * 账户类型（烤火费、自主、信用额度等）
     */
    private String merAccountTypeCode;
    /**
     * 操作金额
     */
    private BigDecimal amount;
    /**
     * 扣款顺序
     */
    private int deductionOrder;
}
