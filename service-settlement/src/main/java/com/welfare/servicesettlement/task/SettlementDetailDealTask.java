package com.welfare.servicesettlement.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.service.MerchantService;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/8 6:35 下午
 * @desc 结算账单明细数据生成任务
 * 每日拉取员工卡交易数据，加工至账单结算明细表中（减少多表关联，提高结算明细查询效率）
 */
@JobHandler(value = "settlementDetailDealTask")
public class SettlementDetailDealTask extends SettlementWorkXxlJobTaskBean<Merchant>{

    @Autowired
    private MerchantMapper merchantMapper;



    @Override
    public List<Merchant> selectTasks(String params) {
        //查询获取待生成商户列表
        List<Merchant> merchants = merchantMapper.selectList(Wrappers.lambdaQuery());
        return merchants;
    }

    @Override
    public void execute(Merchant merchant) {
        //根据商户，生成当日对应商户的结算明细数据

    }
}
