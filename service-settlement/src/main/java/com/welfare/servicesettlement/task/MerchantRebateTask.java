package com.welfare.servicesettlement.task;

import com.alibaba.fastjson.JSON;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.service.SettleDetailService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/19 6:35 下午
 * @desc 商户返点批量任务
 * T+1计算商户返点，并结算至商户返点余额中
 */
@JobHandler(value = "merchantRebateTask")
@Slf4j
@Service
public class MerchantRebateTask extends IJobHandler {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private SettleDetailService settleDetailService;

    @Override
    public ReturnT<String> execute(String param){
        log.info("============商户返点批量任务,任务执行开始===================");

        //查询配置了返利门店的商户列表
        //List<Merchant> merchants = merchantMapper.selectRebateMerList();
        List<Merchant> merchants = new ArrayList<>();
        if(!merchants.isEmpty()){
            log.debug("准备执行任务数据：{}", JSON.toJSONString(merchants));
            try {
                merchants.forEach(merchant->{
                    settleDetailService.merRebate(merchant);
                });
            } catch (Exception e) {
                log.info("============商户返点批量任务,任务执行【异常】===================");
                log.info("任务执行异常,异常信息：{}", e);
                return ReturnT.FAIL;
            }
        }

        log.info("============商户返点批量任务,任务执行【完成】===================");
        return ReturnT.SUCCESS;
    }
}
