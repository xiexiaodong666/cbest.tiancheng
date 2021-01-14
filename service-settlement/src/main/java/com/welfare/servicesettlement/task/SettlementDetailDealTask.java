package com.welfare.servicesettlement.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.PullAccountDetailRecord;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.MerchantMapper;
import com.welfare.persist.mapper.PullAccountDetailRecordMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MerchantService;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Autowired
    private SettleDetailDao settleDetailDao;

    @Autowired
    private SettleDetailMapper settleDetailMapper;

    @Autowired
    private PullAccountDetailRecordMapper pullAccountDetailRecordMapper;

    private static final String DEAL_STATUS_SUCCESS = "success";
    private static final String DEAL_STATUS_FAIL = "fail";



    @Override
    public List<Merchant> selectTasks(String params) {
        //查询获取待生成商户列表
        List<Merchant> merchants = merchantMapper.selectList(Wrappers.lambdaQuery());
        return merchants;
    }

    @Override
    public void execute(Merchant merchant) {
        Date today = new Date();
        Date date = DateUtil.addDays(new Date(), -1);

        //根据商户，拉取员工卡交易数据，生成当日对应商户的结算明细数据

        //插入拉取记录
        PullAccountDetailRecord pullAccountDetailRecord = new PullAccountDetailRecord();
        pullAccountDetailRecord.setDelDate(today);
        pullAccountDetailRecord.setDelStatus(DEAL_STATUS_FAIL);
        pullAccountDetailRecord.setCreateTime(today);
        pullAccountDetailRecord.setCreateUser("system");
        pullAccountDetailRecord.setTryCount(0);
        pullAccountDetailRecordMapper.insert(pullAccountDetailRecord);

        try {
            //拉取数据
            List<SettleDetail> settleDetails = settleDetailMapper.getSettleDetailFromAccountDetail(merchant.getMerCode(), date);
            settleDetailDao.saveOrUpdateBatch(settleDetails);
            pullAccountDetailRecord.setDelStatus(DEAL_STATUS_SUCCESS);
        }catch (Exception e){

        }
        pullAccountDetailRecordMapper.updateById(pullAccountDetailRecord);
    }
}
