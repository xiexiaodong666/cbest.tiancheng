package com.welfare.servicesettlement.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.service.MonthSettleService;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/8 6:35 下午
 * @desc 结算账单生成任务
 * 月初生成上月商户结算账单
 */
@JobHandler(value = "settlementBillBuildTask")
public class SettlementBillBuildTask extends SettlementWorkXxlJobTaskBean<Merchant>{

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MonthSettleService monthSettleService;

    @Override
    public List<Merchant> selectTasks(String params) {
        //查询获取待生成商户列表
        List<Merchant> merchants = merchantMapper.selectList(Wrappers.lambdaQuery());
        return merchants;
    }

    @Override
    public void execute(Merchant merchant) {
        //根据商户，生成对应商户的结算账单
        Date date = new Date();
        Date maxDate = DateUtil.getMonthDayMaxByDate(date);
        Date minDate = DateUtil.getMonthDayMinByDate(date);

        MonthSettleDetailQuery monthSettleDetailQuery = new MonthSettleDetailQuery();
        monthSettleDetailQuery.setMerCode(merchant.getMerCode());
        monthSettleDetailQuery.setStartTime(minDate);
        monthSettleDetailQuery.setEndTime(maxDate);

        MonthSettle monthSettle = monthSettleService.getMonthSettle(monthSettleDetailQuery);

        monthSettleService.addMonthSettle(monthSettle);
    }
}
