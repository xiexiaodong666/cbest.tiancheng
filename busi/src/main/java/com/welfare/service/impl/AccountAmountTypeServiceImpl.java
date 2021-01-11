package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.AccountAmountTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}