package com.welfare.serviceaccount.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.welfare.common.enums.AccountRechargeStatusEnum;
import com.welfare.persist.entity.AccountDepositRecord;
import com.welfare.service.AccountDepositRecordService;
import com.welfare.service.DepositService;
import com.welfare.service.dto.Deposit;
import com.welfare.service.remote.entity.TradeQueryReq;
import com.welfare.service.remote.entity.CbestPayBaseBizResp;
import com.welfare.service.remote.entity.CbestPayRespStatusConstant;
import com.welfare.service.remote.service.CbestPayService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@JobHandler("accountDepositTask")
public class AccountDepositTask extends IJobHandler {

    private final AccountDepositRecordService accountDepositRecordService;

    private final CbestPayService cbestPayService;

    private final DepositService depositService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        execPendingPaymentList();
        execPendingAndFailureRechargeList();
        return ReturnT.SUCCESS;
    }

    /**
     * 处理查询支付结果
     */
    public void execPendingPaymentList() {
        List<AccountDepositRecord> pendingPaymentList = accountDepositRecordService
            .queryPendingPaymentList();
        if (CollectionUtils.isEmpty(pendingPaymentList)) {
            return;
        }
        for (AccountDepositRecord accountDepositRecord : pendingPaymentList) {
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
                        .setPayStatus(AccountRechargeStatusEnum.RECHARGE_SUCCESS.getCode());
                    accountDepositRecord.setPayTime(DateUtil.date());
                    break;
                case CbestPayRespStatusConstant
                    .FAIL:
                    accountDepositRecord
                        .setPayStatus(AccountRechargeStatusEnum.RECHARGE_FAILURE.getCode());
                    log.error(StrUtil.format("查询到支付交易流水号[{}]支付失败", payTradeNo));
                    break;
                default:
                    continue;
            }
            accountDepositRecordService.updateById(accountDepositRecord);
        }
    }

    /**
     * 处理待充值和充值失败的记录
     */
    public void execPendingAndFailureRechargeList() {
        List<AccountDepositRecord> pendingAndFailureRechargeList = accountDepositRecordService
            .queryPendingAndFailureRechargeList();
        if (CollectionUtils.isEmpty(pendingAndFailureRechargeList)) {
            return;
        }
        for (AccountDepositRecord accountDepositRecord : pendingAndFailureRechargeList) {
            Deposit deposit = buildDeposit(accountDepositRecord);
            depositService.deposit(deposit);
            accountDepositRecord
                .setRechargeStatus(
                    AccountRechargeStatusEnum.RECHARGE_SUCCESS.getCode());
            accountDepositRecord.setDepositTime(DateUtil.date());
            accountDepositRecordService.updateById(accountDepositRecord);
        }
    }

    private Deposit buildDeposit(AccountDepositRecord accountDepositRecord) {
        //TODO 构建充值对象
        Deposit deposit = new Deposit();
//        requestId
        deposit.setAccountCode(accountDepositRecord.getAccountCode());
//        cardNo
        deposit.setAmount(accountDepositRecord.getDepositAmount());
        deposit.setMerchantCode(accountDepositRecord.getMerCode());
//            merAccountTypeCode
//        depositStatus
        return deposit;
    }

}
