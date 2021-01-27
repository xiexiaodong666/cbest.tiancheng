package com.welfare.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.Channel;
import com.welfare.common.constants.WelfareConstant.TransType;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dto.AccountBillDetailSimpleDTO;
import com.welfare.persist.dto.query.AccountBillDetailSimpleReq;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountBillDetailService;
import com.welfare.service.dto.Deposit;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户流水明细服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
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

    private final static Map<String, TransType> TRANS_TYPE_MAP = Stream
        .of(TransType.values()).collect(Collectors
            .toMap(TransType::code,
                e -> e));

    private final static Map<String, Channel> CHANNEL_MAP = Stream
        .of(Channel.values()).collect(Collectors
            .toMap(Channel::code,
                e -> e));

    @Override
    public void saveNewAccountBillDetail(Deposit deposit, AccountAmountType accountAmountType,
        Account account) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        Long accountCode = deposit.getAccountCode();
        BigDecimal amount = deposit.getAmount();
        accountBillDetail.setAccountCode(accountCode);
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(deposit.getChannel());
        accountBillDetail.setTransNo(deposit.getTransNo());
        accountBillDetail.setTransAmount(amount);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        //AccountAmountType surplusQuota = accountAmountTypeService.querySurplusQuota(accountCode);
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setTransType(WelfareConstant.TransType.DEPOSIT.code());
        accountBillDetailDao.save(accountBillDetail);
    }


    @Override
    public AccountBillDetail queryByTransNo(String transNo) {
        QueryWrapper<AccountBillDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountBillDetail.TRANS_NO, transNo);
        return accountBillDetailDao.getOne(queryWrapper);
    }

    @Override
    public List<AccountBillDetailSimpleDTO> queryAccountBillDetailSimpleList(
        AccountBillDetailSimpleReq accountBillDetailSimpleReq) {
        List<AccountBillDetailSimpleDTO> accountBillDetailSimpleDTOList = accountBillDetailDao
            .getBaseMapper().selectAccountBillDetailSimpleList(accountBillDetailSimpleReq);

        accountBillDetailSimpleDTOList = accountBillDetailSimpleDTOList.stream()
            .map(accountBillDetailSimpleDTO -> {
                String channel = accountBillDetailSimpleDTO.getChannel();
                String transType = accountBillDetailSimpleDTO.getTransType();
                TransType transTypeEnum = TRANS_TYPE_MAP.get(transType);
                accountBillDetailSimpleDTO.setTransTypeName(transTypeEnum.desc());
                if (transTypeEnum == TransType.DEPOSIT && StrUtil.isNotEmpty(channel)) {
                    Channel channelEnum = CHANNEL_MAP.get(channel);
                    if (channelEnum == Channel.WECHAT || channelEnum == Channel.ALIPAY) {
                        accountBillDetailSimpleDTO.setStoreName("员工自主充值");
                        accountBillDetailSimpleDTO.setTransTypeName(channelEnum.name());
                    }
                }
                return accountBillDetailSimpleDTO;
            }).collect(Collectors.toList());
        return accountBillDetailSimpleDTOList;
    }
}