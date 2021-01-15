package com.welfare.service.impl;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.service.AccountService;
import com.welfare.service.RefundService;
import com.welfare.service.dto.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

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
    private final AccountService accountService;
    @Override
    public void handleRefundRequest(RefundRequest refundRequest) {
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNo(refundRequest.getOriginalTransNo());
        Assert.isTrue(!CollectionUtils.isEmpty(accountDeductionDetails),"未找到正向支付流水");
        AccountDeductionDetail first = accountDeductionDetails.get(0);
        Account account = accountService.getByAccountCode(first.getAccountCode());
        RLock merAccountLock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        merAccountLock.lock();
        try{

        }finally {
            merAccountLock.unlock();
        }
    }

    @Override
    public RefundRequest queryByTransNo(String transNo) {
        return null;
    }
}
