package com.welfare.servicesettlement.task;

import com.welfare.service.settlement.DailyCheckBillService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/7/2021
 */
@Slf4j
@Component("dailyCheckBillTask")
@JobHandler(value = "dailyCheckBillTask")
@RequiredArgsConstructor
public class DailyCheckBillTask extends IJobHandler {
    private final DailyCheckBillService dailyCheckBillService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("start executing dailyCheckBillTask");
        dailyCheckBillService.generateDailyCheckBill();
        log.info("end executing dailyCheckBillTask");
        return ReturnT.SUCCESS;
    }
}
