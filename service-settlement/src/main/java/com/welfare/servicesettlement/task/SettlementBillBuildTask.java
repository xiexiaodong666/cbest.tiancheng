package com.welfare.servicesettlement.task;

import com.welfare.persist.entity.Merchant;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/8 6:35 下午
 * @desc 结算账单生成任务
 * 月初生成上月商户结算账单
 */
@Component("settlementBillBuildTask")
@JobHandler(value = "settlementBillBuildTask")
public class SettlementBillBuildTask extends SettlementWorkXxlJobTaskBean<Merchant>{


    @Override
    public List<Merchant> selectTasks(String params, Integer taskItemNum, Integer taskItem, Integer eachFetchDataNum) {
        //分批查询获取待生成商户列表
        //todo
        return null;
    }

    @Override
    public void execute(List<Merchant> list) {
        //根据商户，生成对应商户的结算账单
        //todo
    }
}
