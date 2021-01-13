package com.welfare.service.impl;

import com.welfare.persist.entity.AccountAmountType;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.PaymentService;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final AccountAmountTypeService accountAmountTypeService;
    @Override
    public List<PaymentOperation> executePay(Long accountCode, BigDecimal amount, String transNo) {
        List<AccountAmountDO> accountAmountDOS = accountAmountTypeService.queryAccountAmountDO(accountCode);

        return null;
    }
}
