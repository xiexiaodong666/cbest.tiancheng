package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dao.AccountDepositApplyDao;
import  com.welfare.persist.dao.AccountDepositApplyDetailDao;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.persist.mapper.AccountDepositApplyDetailMapper;
import com.welfare.service.AccountDepositApplyService;
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

    @Autowired
    private AccountDepositApplyDao accountDepositApplyDao;

    @Autowired
    private AccountDepositApplyDao accountDepositApplyService;

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

    @Override
    public Page<TempAccountDepositApplyDTO> pageById(Long id, int current, int size) {
        Page<AccountDepositApplyDetail> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        AccountDepositApply apply = accountDepositApplyDao.getById(id);
        return accountDepositApplyDetailDao.getBaseMapper().listByApplyCodeIfAccountExist2(page, apply.getApplyCode());
    }

    @Override
    public List<AccountDepositApplyDetail> listByApplyCodeIfAccountExist(String applyCode) {
        return accountDepositApplyDetailDao.getBaseMapper().listByApplyCodeIfAccountExist(applyCode);
    }

    @Override
    public AccountApplyTotalDTO getUserCountAndTotalmount(Long id) {
        AccountDepositApply apply = accountDepositApplyService.getById(id);
        return accountDepositApplyDetailDao.getBaseMapper().getUserCountAndTotalmount(apply.getApplyCode());
    }
}