package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.dto.Deposit;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 用户流水明细服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBillDetailServiceImpl implements AccountBillDetailService {
    private final AccountBillDetailDao accountBillDetailDao;

    /**
     * 循环依赖问题，所以未采用构造器注入
     */
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;
    @Override
    public void saveNewAccountBillDetail(Deposit deposit, AccountAmountType accountAmountType) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        Long accountCode = deposit.getAccountCode();
        BigDecimal amount = deposit.getAmount();
        accountBillDetail.setAccountCode(accountCode);
        accountBillDetail.setAccountBalance(accountAmountType.getAccountBalance().add(amount));
        accountBillDetail.setChannel(deposit.getChannel());
        accountBillDetail.setTransNo(deposit.getTransNo());
        accountBillDetail.setTransAmount(amount);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        AccountAmountType surplusQuota = accountAmountTypeService.querySurplusQuota(accountCode);
        accountBillDetail.setSurplusQuotaBalance(surplusQuota.getAccountBalance());
        accountBillDetail.setTransType(WelfareConstant.TransType.DEPOSIT.code());
        accountBillDetailDao.save(accountBillDetail);
    }


    @Override
    public AccountBillDetail queryByTransNo(String transNo) {
        QueryWrapper<AccountBillDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountBillDetail.TRANS_NO,transNo);
        return accountBillDetailDao.getOne(queryWrapper);
    }
}