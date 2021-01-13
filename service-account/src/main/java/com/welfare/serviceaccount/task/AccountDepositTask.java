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
@JobHandler("accountDepositTask")
public class AccountDepositTask extends IJobHandler {

    private final AccountDepositRecordService accountDepositRecordService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        accountDepositRecordService.execPendingPaymentList();
        return ReturnT.SUCCESS;
    }

}
