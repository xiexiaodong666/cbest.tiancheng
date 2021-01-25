package com.welfare.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dao.MerchantBillDetailDao;
import com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.PullAccountDetailRecord;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.PullAccountDetailRecordMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MerchantBillDetailService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.PullAccountDetailRecordService;
import com.welfare.service.SettleDetailService;
import com.welfare.service.operator.merchant.RebateLimitOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

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
    @Autowired
    private SettleDetailService settleDetailService;
    @Autowired
    private MerchantCreditService merchantCreditService;
    @Autowired
    private MerchantBillDetailDao merchantBillDetailDao;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MerchantCreditDao merchantCreditDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullAccountDetailToSettleDetail(PullAccountDetailRecord pullAccountDetailRecord) {
        log.info("===========拉取账户详细交易数据，请求参数：{}", JSONObject.toJSONString(pullAccountDetailRecord) + "========");
        //拉取数据
        String delDate = pullAccountDetailRecord.getDelDate();

        Date date = null;
        try {
            date = DateUtil.str2DateTime(delDate, DateUtil.DEFAULT_DATE_FORMAT);
        } catch (ParseException e) {
            log.error("date parse error:", e);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtil.getDayMin(date, -1));
        params.put("endTime", DateUtil.getDayMax(date, -1));
        String merCode = pullAccountDetailRecord.getMerCode();
        params.put("merCode", merCode);
        params.put("minId", 0);
        params.put("limit", 2000);

        do {
            log.info("settleDetailMapper循环拉取账户详细交易数据，请求参数：{}", JSONObject.toJSONString(params));
            List<SettleDetail> settleDetails = settleDetailMapper.getSettleDetailFromAccountDetail(params);
            RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
            lock.lock();
            try{
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merCode);
                if (!settleDetails.isEmpty()) {
                    params.put("minId", settleDetails.get(settleDetails.size() - 1).getId() + 1);
                    List<MerchantBillDetail> merchantBillDetails = settleDetailService.calculateAndSetRebate(merchantCredit,settleDetails);
                    List<String> rebateTransNos = merchantBillDetails.stream().map(MerchantBillDetail::getTransNo).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(merchantBillDetails)){
                        //返利需要幂等，先删除相关记录，再重新保存。
                        merchantBillDetailDao.deleteByTransNoAndBalanceType(rebateTransNos, MerCreditType.REBATE_LIMIT.code());
                        merchantBillDetailDao.saveBatch(merchantBillDetails);
                    }
                    merchantCreditDao.updateById(merchantCredit);
                    settleDetailDao.saveBatch(settleDetails);
                } else {
                    break;
                }
            }finally {
                lock.unlock();
            }

        } while (true);

        pullAccountDetailRecord.setDelStatus(WelfareSettleConstant.PullTaksSendStatusEnum.SUCCESS.code());
        pullAccountDetailRecordMapper.updateById(pullAccountDetailRecord);
    }
}
