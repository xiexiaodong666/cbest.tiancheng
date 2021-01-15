package com.welfare.service.impl;

import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.service.RefundService;
import com.welfare.service.dto.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/15/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RefundServiceImpl implements RefundService {
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final RedissonClient redissonClient;

    @Override
    public void handleRefundRequest(RefundRequest refundRequest) {
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNo(refundRequest.getOriginalTransNo());

    }

    @Override
    public RefundRequest queryByTransNo(String transNo) {
        return null;
    }
}
