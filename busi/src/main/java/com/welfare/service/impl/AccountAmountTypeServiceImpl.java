package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountBillDetailService;
import com.welfare.service.dto.Deposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:29 PM
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountAmountTypeServiceImpl implements AccountAmountTypeService {
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountAmountTypeMapper accountAmountTypeMapper;
    /**
     * 循环依赖问题，所以未采用构造器注入
     */
    @Autowired
    private AccountBillDetailService accountBillDetailService;

    @Override
    public int batchSaveOrUpdate(List<AccountAmountType> list) {
        return accountAmountTypeMapper.batchSaveOrUpdate(list);
    }

    @Override
    public AccountAmountType queryOne(String accountCode, String merAccountTypeCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, accountCode)
                .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, merAccountTypeCode);
        return accountAmountTypeDao.getOne(queryWrapper);

    }

    @Override
    public void updateAccountAmountType(Deposit deposit) {
        AccountAmountType accountAmountType = this.queryOne(deposit.getAccountCode(),
                deposit.getMerAccountTypeCode());

        if (Objects.isNull(accountAmountType)) {
            accountAmountType = deposit.toNewAccountAmountType();
            BigDecimal afterAddAmount = accountAmountType.getAccountBalance().add(deposit.getAmount());
            accountAmountType.setAccountBalance(afterAddAmount);
            accountAmountTypeDao.save(accountAmountType);
        } else {
            accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(deposit.getAmount()));
            accountAmountTypeDao.updateById(accountAmountType);
        }
        accountBillDetailService.saveNewAccountBillDetail(deposit, accountAmountType);

    }

    @Override
    public AccountAmountType querySurplusQuota(String accountCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE,accountCode)
                .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA);
        return accountAmountTypeDao.getOne(queryWrapper);
    }



}