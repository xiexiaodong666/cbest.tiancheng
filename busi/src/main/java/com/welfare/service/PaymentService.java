package com.welfare.service;

import com.welfare.service.operator.payment.domain.PaymentOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 扣款相关service
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
public interface PaymentService {
    /**
     * 扣款
     * @param accountCode
     * @param amount
     * @param transNo
     * @return
     */
    List<PaymentOperation> executePay(Long accountCode, BigDecimal amount, String transNo);
}
