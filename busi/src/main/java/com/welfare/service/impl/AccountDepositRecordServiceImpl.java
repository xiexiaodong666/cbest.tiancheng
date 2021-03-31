package com.welfare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.common.config.CbestPayConfig;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerAccountTypeCode;
import com.welfare.common.constants.WelfareConstant.SequenceType;
import com.welfare.common.enums.AccountPayTypeEnum;
import com.welfare.common.enums.AccountRechargePaymentStatusEnum;
import com.welfare.common.enums.AccountRechargeStatusEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDepositRecord;
import com.welfare.persist.mapper.AccountDepositRecordMapper;
import com.welfare.service.AccountDepositRecordService;
import com.welfare.service.AccountService;
import com.welfare.service.DepositService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.*;
import com.welfare.service.remote.entity.*;
import com.welfare.service.remote.service.CbestPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 账号充值记录表服务接口实现
 *
 * @author kancy
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-11 09:20:53
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountDepositRecordServiceImpl extends
    ServiceImpl<AccountDepositRecordMapper, AccountDepositRecord> implements
    AccountDepositRecordService {

    private final AccountService accountService;

    private final CbestPayService cbestPayService;

    private final DepositService depositService;

    private final CbestPayConfig cbestPayConfig;

    private final SequenceService sequenceService;

    @Override
    public AccountDepositDTO getPayInfo(AccountDepositReq req) {
        String payType = req.getPayType();
        AccountPayTypeEnum accountPayTypeEnum = AccountPayTypeEnum.getByType(payType);
        if (accountPayTypeEnum == null) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "支付方式有误", null);
        }
        Long accountCode = req.getAccountCode();
        Account account = accountService.getByAccountCode(accountCode);
        if (account == null) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "获取用户信息失败", null);
        }

        //组装参数，请求微信H5交易创建接口
        CreateWXH5TradeReq createWXH5TradeReq = new CreateWXH5TradeReq();
//        String payTradeNo = IdGenerator.nextIdStr();
        //生成交易流水号
        Long nextNo = sequenceService.nextNo(SequenceType.DEPOSIT.code());
        String payTradeNo = Long.toString(nextNo);
        BigDecimal rechargeAmount = req.getRechargeAmount();
        createWXH5TradeReq.setTradeNo(payTradeNo);
        createWXH5TradeReq.setAmount(amountToFen(rechargeAmount));
        createWXH5TradeReq.setNotifyUrl(req.getNotifyUrl());
        String market = account.getMerCode();

        CbestPayBaseBizResp resp = cbestPayService.createWXH5Trade(market, createWXH5TradeReq);
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error(
                StrUtil.format("调用重百付微信H5交易创建接口异常-req: {}, resp: {}", JSON.toJSONString(req),
                    JSON.toJSONString(resp)));
            throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        }
        //保存支付信息
        AccountDepositRecord accountDepositRecord = new AccountDepositRecord();
        accountDepositRecord.setAccountCode(account.getAccountCode());
        accountDepositRecord.setPayType(accountPayTypeEnum.getType());
        accountDepositRecord.setMerCode(account.getMerCode());
        accountDepositRecord
            .setPayStatus(AccountRechargePaymentStatusEnum.PENDING_PAYMENT.getCode());
        accountDepositRecord
            .setRechargeStatus(AccountRechargeStatusEnum.PENDING_RECHARGE.getCode());
        accountDepositRecord.setPayTradeNo(payTradeNo);
        accountDepositRecord.setDepositAmount(rechargeAmount);
        accountDepositRecord.setCreateTime(DateUtil.date());
        accountDepositRecord.setUpdateTime(DateUtil.date());
        CreateWXH5TradeResp createWXH5TradeResp = JSON
            .parseObject(resp.getBizContent(), CreateWXH5TradeResp.class);
        accountDepositRecord.setPayGatewayTradeNo(createWXH5TradeResp.getGatewayTradeNo());
        accountDepositRecord.setPayChannelTradeNo(createWXH5TradeResp.getChannelTradeNo());

        boolean saved = save(accountDepositRecord);
        if (!saved) {
            log.error(StrUtil.format("保存支付信息失败-入参：{}", JSON.toJSONString(req)));
            throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "保存支付信息失败", null);
        }
        //返回支付流水号和支付链接
        AccountDepositDTO accountDepositDTO = new AccountDepositDTO();
        accountDepositDTO.setPayTradeNo(payTradeNo);
        accountDepositDTO.setH5Url(createWXH5TradeResp.getH5Url());
        return accountDepositDTO;
    }

    private int amountToFen(BigDecimal rechargeAmount) {
        return BigDecimal.valueOf(100).multiply(rechargeAmount).intValue();
    }

    @Override
    public AccountPayResultQueryDTO queryPayResult(AccountPayResultQueryReq req) {
        String payTradeNo = req.getPayTradeNo();
        AccountDepositRecord accountDepositRecord = getOne(
            Wrappers.<AccountDepositRecord>lambdaQuery()
                .eq(AccountDepositRecord::getPayTradeNo, payTradeNo));

        Integer payStatus = accountDepositRecord.getPayStatus();
        AccountRechargePaymentStatusEnum accountRechargePaymentStatusEnum = AccountRechargePaymentStatusEnum
            .getByCode(payStatus);

        AccountPayResultQueryDTO accountPayResultQueryDTO = new AccountPayResultQueryDTO();
        accountPayResultQueryDTO.setPaymentStatus(
            accountRechargePaymentStatusEnum.name());
        return accountPayResultQueryDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payNotify(CbestPayBaseResp resp) {
        log.info(StrUtil.format("重百付支付回调响应-resp: {}", JSON.toJSONString(resp)));
        String status = resp.getStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(status)) {
            log.error(StrUtil.format("重百付支付回调响应异常-resp: {}", JSON.toJSONString(resp)));
            return;
        }
        CreateWXH5TradeNotifyResp createWXH5TradeNotifyResp = JSON
            .parseObject(resp.getBizContent(), CreateWXH5TradeNotifyResp.class);
        String payTradeNo = createWXH5TradeNotifyResp.getTradeNo();
        AccountDepositRecord accountDepositRecord = getOne(
            Wrappers.<AccountDepositRecord>lambdaQuery()
                .eq(AccountDepositRecord::getPayTradeNo, payTradeNo)
                .eq(AccountDepositRecord::getPayStatus,
                    AccountRechargePaymentStatusEnum.PENDING_PAYMENT.getCode()));
        if (accountDepositRecord == null) {
            return;
        }
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            accountDepositRecord
                .setPayStatus(AccountRechargePaymentStatusEnum.PAYMENT_FAILURE.getCode());
            updateById(accountDepositRecord);
            return;
        }
        Map<String, Object> map = BeanUtil.beanToMap(resp, true, true);
        map.remove("sign");
        String sign = map.entrySet().stream()
            .sorted(Entry.comparingByKey())
            .map(item -> item.getKey() + "=" + item.getValue())
            .collect(Collectors.joining("&"))
            .concat(cbestPayConfig.getAppKey());
        String md5Sign = SecureUtil.md5(sign);
        if (!md5Sign.equalsIgnoreCase(resp.getSign())) {
            log.error(
                StrUtil.format("重百付支付回调验签失败-resp: {}, 加密前sign: {}, 加密后的md5Sign: {}",
                    JSON.toJSONString(resp), sign, md5Sign));
            return;
        }

        //更新支付状态
        accountDepositRecord
            .setPayStatus(AccountRechargePaymentStatusEnum.PAYMENT_SUCCESS.getCode());
        accountDepositRecord.setPayTime(DateUtil.date());
        updateById(accountDepositRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execPendingPaymentList() {
        List<AccountDepositRecord> accountDepositRecordList = list(
            Wrappers.<AccountDepositRecord>lambdaQuery()
                .eq(AccountDepositRecord::getPayStatus,
                    AccountRechargePaymentStatusEnum.PENDING_PAYMENT.getCode()));
        if (CollectionUtils.isEmpty(accountDepositRecordList)) {
            return;
        }
        for (AccountDepositRecord accountDepositRecord : accountDepositRecordList) {
            TradeQueryReq req = new TradeQueryReq();
            String payTradeNo = accountDepositRecord.getPayTradeNo();
            req.setTradeNo(payTradeNo);
            req.setGatewayTradeNo(accountDepositRecord.getPayGatewayTradeNo());
            CbestPayBaseBizResp baseBizResp = cbestPayService
                .tradeQuery(accountDepositRecord.getMerCode(), req);
            String bizStatus = baseBizResp.getBizStatus();

            switch (bizStatus) {
                case CbestPayRespStatusConstant
                    .SUCCESS:
                    accountDepositRecord
                        .setPayStatus(AccountRechargePaymentStatusEnum.PAYMENT_SUCCESS.getCode());
                    accountDepositRecord.setPayTime(DateUtil.date());
                    break;
                case CbestPayRespStatusConstant
                    .FAIL:
                    accountDepositRecord
                        .setPayStatus(AccountRechargePaymentStatusEnum.PAYMENT_FAILURE.getCode());
                    log.error(StrUtil.format("查询到支付交易流水号[{}]支付失败", payTradeNo));
                    break;
                default:
                    if (DateUtil
                        .between(accountDepositRecord.getCreateTime(), new Date(),
                            DateUnit.MINUTE) > 6) {
                        accountDepositRecord
                            .setPayStatus(
                                AccountRechargePaymentStatusEnum.QUERY_PAY_RESULT_NOT_FOUND
                                    .getCode());
                        log.error(StrUtil.format("超过6分钟未查询到支付交易流水号[{}]的支付结果", payTradeNo));
                    } else {
                        continue;
                    }
                    break;
            }
            updateById(accountDepositRecord);
        }
    }

    private Deposit buildDeposit(AccountDepositRecord accountDepositRecord) {
        Deposit deposit = new Deposit();
        Long transNo = sequenceService.nextNo(WelfareConstant.SequenceType.DEPOSIT.code());
        deposit.setTransNo(Long.toString(transNo));
        deposit.setAccountCode(accountDepositRecord.getAccountCode());
        deposit.setAmount(accountDepositRecord.getDepositAmount());
        deposit.setMerchantCode(accountDepositRecord.getMerCode());
        deposit.setMerAccountTypeCode(MerAccountTypeCode.SELF.code());
        deposit.setChannel(accountDepositRecord.getPayType());
        return deposit;
    }

    @Override
    public void execPendingAndFailureRechargeList() {
        List<AccountDepositRecord> pendingAndFailureRechargeList = list(
            Wrappers.<AccountDepositRecord>lambdaQuery()
                .eq(AccountDepositRecord::getPayStatus,
                    AccountRechargePaymentStatusEnum.PAYMENT_SUCCESS.getCode())
                .in(AccountDepositRecord::getRechargeStatus,
                    AccountRechargeStatusEnum.PENDING_RECHARGE.getCode(),
                    AccountRechargeStatusEnum.RECHARGE_FAILURE.getCode()));
        if (CollectionUtils.isEmpty(pendingAndFailureRechargeList)) {
            return;
        }
        for (AccountDepositRecord accountDepositRecord : pendingAndFailureRechargeList) {
            try {
                Deposit deposit = buildDeposit(accountDepositRecord);
                depositService.personalDeposit(deposit);
                accountDepositRecord.setDepositTradeNo(deposit.getTransNo());
                accountDepositRecord
                    .setRechargeStatus(
                        AccountRechargeStatusEnum.RECHARGE_SUCCESS.getCode());
                accountDepositRecord.setDepositTime(DateUtil.date());
                updateById(accountDepositRecord);
            } catch (BizException e) {
                log.error("支付成功后充值业务异常", e);
                if (DateUtil
                    .between(accountDepositRecord.getCreateTime(), new Date(),
                        DateUnit.MINUTE) > 5) {
                    log.error(StrUtil.format("超过5分钟未充值成功，发起支付冲正-accountDepositRecord:{}" + JSON
                        .toJSONString(accountDepositRecord)));
                    //5分支查询还未充值成功，发起支付冲正
                    String payTradeNo = accountDepositRecord.getPayTradeNo();
                    log.info(StrUtil.format("支付交易流水号[{}]5分支还未充值成功，调用支付冲正接口", payTradeNo));
                    TradeCancelReq tradeCancelReq = new TradeCancelReq();
                    tradeCancelReq.setTradeNo(payTradeNo);
                    tradeCancelReq
                        .setGatewayTradeNo(accountDepositRecord.getPayGatewayTradeNo());
                    String market = accountDepositRecord.getMerCode();
                    CbestPayBaseBizResp baseBizResp = cbestPayService
                        .tradeCancel(market, tradeCancelReq);
                    String tradeCancelBizStatus = baseBizResp.getBizStatus();
                    if (CbestPayRespStatusConstant
                        .SUCCESS.equals(tradeCancelBizStatus)) {
                        accountDepositRecord.setPayStatus(
                            AccountRechargePaymentStatusEnum.REFUND_SUCCESS.getCode());

                        log.info(StrUtil.format("支付交易流水号[{}]调用支付冲正接口成功", payTradeNo));
                        updateById(accountDepositRecord);
                    } else if (CbestPayRespStatusConstant
                        .FAIL.equals(tradeCancelBizStatus)) {
                        accountDepositRecord.setPayStatus(
                            AccountRechargePaymentStatusEnum.REFUND_FAILURE.getCode());
                        log.error(StrUtil.format("支付交易流水号[{}]调用支付冲正接口失败", payTradeNo));
                        updateById(accountDepositRecord);
                    } else {
                        if (DateUtil
                            .between(accountDepositRecord.getCreateTime(), new Date(),
                                DateUnit.MINUTE) > 10) {
                            accountDepositRecord.setPayStatus(
                                AccountRechargePaymentStatusEnum.REFUND_TIMEOUT.getCode());
                            log.error(
                                StrUtil.format("支付交易流水号[{}]调用支付冲正接口超过5分钟未返回明确成功或失败", payTradeNo));
                            updateById(accountDepositRecord);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("支付回调后充值其它异常", e);
            }
        }
    }

}