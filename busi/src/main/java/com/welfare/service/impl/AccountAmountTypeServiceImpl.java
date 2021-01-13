package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountBillDetailService;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.dto.Deposit;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final MerchantAccountTypeService merchantAccountTypeService;
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
    public AccountAmountType queryOne(Long accountCode, String merAccountTypeCode) {
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
    public AccountAmountType querySurplusQuota(Long accountCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, accountCode)
                .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA);
        return accountAmountTypeDao.getOne(queryWrapper);
    }

    @Override
    public List<AccountAmountDO> queryAccountAmountDO(Account account) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, account.getAccountCode());
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(queryWrapper);
        Assert.isTrue(!CollectionUtils.isEmpty(accountAmountTypes), "该用户没有账户余额信息");
        List<MerchantAccountType> types = merchantAccountTypeService.queryByMerCode(account.getMerCode());
        Assert.isTrue(!CollectionUtils.isEmpty(types), "该商户没有配置accountType");

        Map<String, MerchantAccountType> map = types.stream()
                .collect(Collectors.toMap(MerchantAccountType::getMerAccountTypeCode, type -> type));
        return accountAmountTypes.stream()
                .map(accountAmountType ->
                        AccountAmountDO.of(accountAmountType, map.get(accountAmountType.getMerAccountTypeCode())))
                .collect(Collectors.toList());
    }


}