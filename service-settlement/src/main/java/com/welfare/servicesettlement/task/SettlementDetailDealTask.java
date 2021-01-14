package com.welfare.servicesettlement.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dao.PullAccountDetailRecordDao;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.entity.PullAccountDetailRecord;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.persist.mapper.PullAccountDetailRecordMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MerchantService;
import com.welfare.service.PullAccountDetailRecordService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/8 6:35 下午
 * @desc 结算账单明细数据生成任务
 * 每日拉取员工卡交易数据，加工至账单结算明细表中（减少多表关联，提高结算明细查询效率）
 */
@Slf4j
@Service
@JobHandler(value = "settlementDetailDealTask")
public class SettlementDetailDealTask extends IJobHandler {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private PullAccountDetailRecordService pullAccountDetailRecordService;

    @Autowired
    private PullAccountDetailRecordMapper pullAccountDetailRecordMapper;

    @Autowired
    private PullAccountDetailRecordDao pullAccountDetailRecordDao;


    public ReturnT<String> execute(String param){
        log.info("============结算账单明细数据生成任务,任务执行开始===================");
        log.info("请求参数：{}",param);

        Date today = new Date();
        if(StringUtils.isEmpty(param)){
            JSONObject jsonObject = JSON.parseObject(param);
            try {
                String date = jsonObject.getString("date");
                today = DateUtil.str2Date(date, DateUtil.DEFAULT_DATE_FORMAT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Merchant> merchants = merchantMapper.selectList(Wrappers.lambdaQuery());
        if(!merchants.isEmpty()){
            log.debug("准备执行任务数据：{}", JSON.toJSONString(merchants));
        }
        try {
            List<PullAccountDetailRecord> pullAccountDetailRecords = new ArrayList<>();
            Date finalToday = today;
            merchants.forEach(merchant->{
                //插入拉取记录
                PullAccountDetailRecord pullAccountDetailRecord = new PullAccountDetailRecord();
                pullAccountDetailRecord.setDelDate(DateUtil.date2Str(finalToday, DateUtil.DEFAULT_DATE_FORMAT));
                pullAccountDetailRecord.setDelStatus(WelfareSettleConstant.PullTaksSendStatusEnum.FAIL.code());
                pullAccountDetailRecord.setCreateTime(finalToday);
                pullAccountDetailRecord.setCreateUser("system");
                pullAccountDetailRecord.setTryCount(0);
                 pullAccountDetailRecord.setMerCode(merchant.getMerCode());
                pullAccountDetailRecords.add(pullAccountDetailRecord);
            });
            pullAccountDetailRecordDao.saveBatch(pullAccountDetailRecords);



            //任务处理
            List<PullAccountDetailRecord> pullAccountDetailRecordList = pullAccountDetailRecordMapper.selectList(Wrappers.<PullAccountDetailRecord>lambdaQuery()
                    .eq(PullAccountDetailRecord::getDelStatus, WelfareSettleConstant.PullTaksSendStatusEnum.FAIL.code())
            );

            pullAccountDetailRecordList.forEach(pullAccountDetailRecord -> {
                pullAccountDetailRecord.setTryCount(pullAccountDetailRecord.getTryCount()+1);
                pullAccountDetailRecordMapper.updateById(pullAccountDetailRecord);
                pullAccountDetailRecordService.pullAccountDetailToSettleDetail(pullAccountDetailRecord);
            });
        } catch (Exception e) {
            log.info("============结算账单明细数据生成任务,任务执行【异常】===================");
            log.info("任务执行异常,异常信息：{}", e);
            return ReturnT.FAIL;
        }

        log.info("============结算账单明细数据生成任务,任务执行【完成】===================");
        return ReturnT.SUCCESS;
    }
}
