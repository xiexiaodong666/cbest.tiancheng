package com.welfare.service;

import com.welfare.persist.entity.PullAccountDetailRecord;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/14 2:41 下午
 * @desc
 */
public interface PullAccountDetailRecordService {

    /**
     * 拉取账户交易明细至结算明细表中
     * @param pullAccountDetailRecord
     */
    void pullAccountDetailToSettleDetail(PullAccountDetailRecord pullAccountDetailRecord);

}
