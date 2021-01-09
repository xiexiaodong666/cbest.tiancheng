package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import  com.welfare.persist.dao.AccountDepositApplyDetailDao;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountDepositApplyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 充值申请明细服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountDepositApplyDetailServiceImpl implements AccountDepositApplyDetailService {

    @Autowired
    private AccountDepositApplyDetailDao accountDepositApplyDetailDao;

    @Override
    public List<AccountDepositApplyDetail> listByApplyCode(String applyCode) {
        QueryWrapper<AccountDepositApplyDetail> query = new QueryWrapper<>();
        query.eq(AccountDepositApplyDetail.APPLY_CODE, applyCode);
        return accountDepositApplyDetailDao.list(query);
    }

    @Override
    public Boolean delByApplyCode(String applyCode) {
        QueryWrapper<AccountDepositApplyDetail> query = new QueryWrapper<>();
        query.eq(AccountDepositApplyDetail.APPLY_CODE, applyCode);
        return accountDepositApplyDetailDao.remove(query);
    }
}