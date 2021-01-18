package com.welfare.serviceaccount.task;

import com.welfare.service.AccountDepositRecordService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@JobHandler("accountDepositPaymentResultTask")
public class AccountDepositPaymentResultTask extends IJobHandler {
    private final AccountDepositRecordService accountDepositRecordService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            log.info("============{}查询充值结果任务执行开始===================", this.getClass().getName());
            accountDepositRecordService.execPendingPaymentList();
        } catch (Exception e) {
            log.info("============{}查询充值结果任务执行【异常】===================", this.getClass().getName());
            log.error("查询充值结果执行异常", e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
