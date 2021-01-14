package com.welfare.service.impl;

import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dao.PullAccountDetailRecordDao;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.entity.PullAccountDetailRecord;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.PullAccountDetailRecordMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.PullAccountDetailRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/14 2:41 下午
 * @desc
 */
@Service
@Slf4j
public class PullAccountDetailRecordServiceImpl implements PullAccountDetailRecordService {

    @Autowired
    private SettleDetailMapper settleDetailMapper;

    @Autowired
    private SettleDetailDao settleDetailDao;

    @Autowired
    private PullAccountDetailRecordMapper pullAccountDetailRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullAccountDetailToSettleDetail(PullAccountDetailRecord pullAccountDetailRecord) {

        //拉取数据
        String delDate = pullAccountDetailRecord.getDelDate();

        Date date = null;
        try {
            date = DateUtil.str2DateTime(delDate, DateUtil.DEFAULT_DATE_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startTime",DateUtil.getDayMin(date, -1));
        params.put("endTime",DateUtil.getDayMax(date, -1));
        params.put("merCode",pullAccountDetailRecord.getMerCode());
        params.put("minId",0);
        params.put("limit",2000);

        do {
            List<SettleDetail> settleDetails = settleDetailMapper.getSettleDetailFromAccountDetail(params);
            if(!settleDetails.isEmpty()){
                settleDetailDao.saveOrUpdateBatch(settleDetails);
                params.put("minId",settleDetails.get(settleDetails.size()-1).getId()+1);
            }else{
                break;
            }
        }while(true);

        pullAccountDetailRecord.setDelStatus(WelfareSettleConstant.PullTaksSendStatusEnum.SUCCESS.code());
        pullAccountDetailRecordMapper.updateById(pullAccountDetailRecord);
    }
}
