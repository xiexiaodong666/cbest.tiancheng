package com.welfare.service.operator.payment.domain;

import com.welfare.persist.entity.*;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.Data;

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
    private AccountAmountTypeGroup accountAmountTypeGroup;
    private AccountBillDetail accountBillDetail;
    private AccountDeductionDetail accountDeductionDetail;
    private List<MerchantAccountOperation> merchantAccountOperations;

    private BigDecimal operateAmount;
    private String transNo;
    private boolean enough;
}
