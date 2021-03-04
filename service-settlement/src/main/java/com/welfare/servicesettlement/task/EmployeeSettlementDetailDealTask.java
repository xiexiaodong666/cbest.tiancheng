package com.welfare.servicesettlement.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.service.PullAccountDetailRecordService;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 员工授信账号明细数据生成任务
 * 每日拉取员工卡授信交易数据，加工至员工授信账单结算明细表中
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/4 1:51 下午
 */
@Slf4j
@Service
@JobHandler(value = "employeeSettlementDetailDealTask")
public class EmployeeSettlementDetailDealTask extends IJobHandler {

  @Autowired
  private EmployeeSettleDetailService employeeSettleDetailService;

  @Override
  public ReturnT<String> execute(String param){
    log.info("============员工授信结算账单明细数据生成任务,任务执行开始===================");
    log.info("请求参数：{}",param);

    Date today = new Date();
    if(!StringUtils.isEmpty(param)){
      JSONObject jsonObject = JSON.parseObject(param);
      try {
        String date = jsonObject.getString("date");
        today = DateUtil.str2Date(date, DateUtil.DEFAULT_DATE_FORMAT);
      } catch (Exception e) {
        log.error("exception when parse date:",e);
      }
    }
    String dateStr = DateUtil.date2Str(today,  DateUtil.DEFAULT_DATE_FORMAT);
    try {
      employeeSettleDetailService.pullAccountDetailByDate(today);
    } catch (Exception e) {
      log.error("员工授信结算账单明细数据生成任务异常,日期:{},异常信息", dateStr, e);
      return ReturnT.FAIL;
    }
    log.info("============员工授信结算账单明细数据生成任务,日期:{} 任务执行【完成】===================", dateStr);
    return ReturnT.SUCCESS;
  }
}
