package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dto.AccountBillDetailMapperDTO;
import com.welfare.persist.dto.AccountBillMapperDTO;
import com.welfare.persist.dto.AccountDetailMapperDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.service.converter.AccountConverter;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountPageReq;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户信息服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    @Autowired
    private final AccountMapper accountMapper;
    private final AccountCustomizeMapper accountCustomizeMapper;
    private final AccountConverter accountConverter;


    @Override
    public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
        IPage<AccountPageDTO> iPage = accountCustomizeMapper.queryPageDTO(page,accountPageReq.getMerCode(),accountPageReq.getAccountName(),accountPageReq.getDepartmentCode(),accountPageReq.getAccountStatus(),accountPageReq.getAccountTypeCode());
        return accountConverter.toPage(iPage);
    }

    @Override
    public int increaseAccountBalance(BigDecimal increaseBalance, String updateUser, String accountCode) {
        return accountMapper.increaseAccountBalance(increaseBalance, updateUser, accountCode);
    }

    @Override
    public Account getByAccountCode(String accountCode) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Account.ACCOUNT_CODE, accountCode);
        return accountDao.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(Account.ID,id);
        Account account = new Account();
        account.setDeleted(true);
        return accountDao.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean active(Long id, Integer active) {
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(Account.ID,id);
        Account account = new Account();
        account.setActive(active);
        return accountDao.update(updateWrapper);
    }

    @Override
    public AccountDetailDTO queryDetail(Long id) {
        AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper.queryDetail(id);
        AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
        BeanUtils.copyProperties(accountDetailMapperDTO,accountDetailDTO);
        return accountDetailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(Account account) {
        return accountDao.save(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(Account account) {
        return accountDao.updateById(account);
    }

    @Override
    public Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage,Integer pageSize,
        String accountCode, Date createTimeStart, Date createTimeEnd) {
        Page<AccountBillDetailMapperDTO> page = new Page<>(currentPage,pageSize);
        IPage<AccountBillDetailMapperDTO> iPage = accountCustomizeMapper.queryAccountBillDetail(page,accountCode,createTimeStart,createTimeEnd);
        return accountConverter.toBillDetailPage(iPage);
    }

    @Override
    public AccountBillDTO quertBill(String accountCode, Date createTimeStart, Date createTimeEnd) {
        AccountBillDTO accountBillDTO = new AccountBillDTO();
        AccountBillMapperDTO accountBillMapperDTO =accountCustomizeMapper.queryBill(accountCode,createTimeStart,createTimeEnd);
        BeanUtils.copyProperties(accountBillMapperDTO,accountBillDTO);
        return accountBillDTO;
    }
}