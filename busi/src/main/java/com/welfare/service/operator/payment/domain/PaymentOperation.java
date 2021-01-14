package com.welfare.service.operator.payment.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.Data;
import org.springframework.core.Ordered;

import java.math.BigDecimal;
import java.util.List;

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
    private AccountBillDetail accountBillDetail;
    private AccountDeductionDetail accountDeductionDetail;
    private List<MerchantAccountOperation> merchantAccountOperations;

    private BigDecimal operateAmount;
    private String transNo;
    private boolean enough;
}
