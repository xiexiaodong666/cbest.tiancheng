package com.welfare.service.settlement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.settlement.SettleDetailGenerateService;
import com.welfare.service.settlement.domain.WholesaleDetail;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Service
@RequiredArgsConstructor
public class SettleDetailGenerateServiceImpl implements SettleDetailGenerateService {
    private final WholesaleReceivableSettleDetailDao receivableDetailDao;
    private final WholesalePayableSettleDetailDao payableDetailDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;

    @Override
    public void generateWholesaleDetails(List<Long> accountDeductionDetailIds) {
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.listByIds(accountDeductionDetailIds);

        List<WholesaleDetail> wholesaleDetails = accountDeductionDetails.stream().map(accountDeductionDetail -> {
            if (!WelfareConstant.MerAccountTypeCode.WHOLESALE.code().equals(accountDeductionDetail.getMerAccountType())) {
                return null;
            }
            List<WholesaleReceivableSettleDetail> settleDetailsInDb = receivableDetailDao.queryByTransNo(accountDeductionDetail.getTransNo());
            if(CollectionUtil.isNotEmpty(settleDetailsInDb) && accountDeductionDetails.size() == settleDetailsInDb.size()){
                //已经处理过，则不处理（MQ重试的时候会到这个逻辑）
                return null;
            }else if(CollectionUtil.isNotEmpty(settleDetailsInDb) && accountDeductionDetails.size() != settleDetailsInDb.size()){
                throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION,"数据库中的结算明细和付款明细数量不一致");
            }
            return WholesaleDetail.fromAccountDeductionDetail(accountDeductionDetail);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<WholesalePayableSettleDetail> payableDetails = wholesaleDetails.stream()
                .map(WholesaleDetail::toWholesalePayableDetail)
                .collect(Collectors.toList());

        List<WholesaleReceivableSettleDetail> receivableDetails = wholesaleDetails.stream()
                .map(WholesaleDetail::toWholesaleReceivableDetail)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(payableDetails)) {
            payableDetailDao.saveBatch(payableDetails);
        }
        if (CollectionUtils.isNotEmpty(receivableDetails)) {
            receivableDetailDao.saveBatch(receivableDetails);
        }
    }
}
