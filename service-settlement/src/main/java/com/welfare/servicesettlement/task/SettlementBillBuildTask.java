package com.welfare.servicesettlement.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.service.MonthSettleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
@Slf4j
@Service
public class SettlementBillBuildTask extends IJobHandler {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MonthSettleService monthSettleService;

    @Override
    public ReturnT<String> execute(String param){
        log.info("============结算账单生成任务,任务执行开始===================");

        //处理数据
        log.info("请求参数：{}",param);

        Date today = new Date();
        if(StringUtils.isEmpty(param)){
            JSONObject jsonObject = JSON.parseObject(param);
            try {
                String date = jsonObject.getString("date");
                today = DateUtil.str2Date(date, DateUtil.DEFAULT_DATE_FORMAT);
            } catch (Exception e) {
                log.error("Exception detected when parse date:",e);
            }
        }

        //查询获取待生成商户列表
        List<Merchant> merchants = merchantMapper.selectList(Wrappers.lambdaQuery());

        List<MonthSettle> monthSettleList = new ArrayList<>();
        Date maxDate = DateUtil.getMonthDayMaxByDate(today);
        Date minDate = DateUtil.getMonthDayMinByDate(today);

        if(!merchants.isEmpty()){
            log.debug("准备执行任务数据：{}", JSON.toJSONString(merchants));
            try {
                Date finalToday = today;
                merchants.forEach(merchant->{
                    //根据商户，生成对应商户的结算账单
                    MonthSettleDetailQuery monthSettleDetailQuery = new MonthSettleDetailQuery();
                    monthSettleDetailQuery.setMerCode(merchant.getMerCode());
                    monthSettleDetailQuery.setStartTime(minDate);
                    monthSettleDetailQuery.setEndTime(maxDate);

                    MonthSettle monthSettle = monthSettleService.getMonthSettle(monthSettleDetailQuery);
                    if(monthSettle==null){
                        //生成默认结算单
                        monthSettle = new MonthSettle();
                        monthSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code());
                        monthSettle.setRecStatus(WelfareSettleConstant.SettleRecStatusEnum.UNCONFIRMED.code());
                        monthSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
                        monthSettle.setMerCode(merchant.getMerCode());
                        monthSettle.setSettleNo(merchant.getMerCode()+DateUtil.date2Str(finalToday, DateUtil.PURE_FORMATE_MONTH));
                        monthSettle.setSettleMonth(DateUtil.date2Str(finalToday, DateUtil.FORMAT_YEAR_MONTH));
                        monthSettle.setRebateAmount(new BigDecimal(0));
                        monthSettle.setSettleAmount(new BigDecimal(0));
                        monthSettle.setOrderNum(0);
                        monthSettle.setCreateTime(finalToday);
                        monthSettle.setCreateUser("system");
                    }
                    monthSettleList.add(monthSettle);
                });
                Boolean aBoolean = monthSettleService.addMonthSettleList(monthSettleList);
                if(!aBoolean){
                    throw new BusiException(null, "批量保存账单数据异常", null);
                }
            } catch (Exception e) {
                log.info("============结算账单生成任务,任务执行【异常】===================");
                log.info("任务执行异常,异常信息：{}", e);
                return ReturnT.FAIL;
            }
        }

        log.info("============结算账单生成任务,任务执行【完成】===================");
        return ReturnT.SUCCESS;
    }
}
