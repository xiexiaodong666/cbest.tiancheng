package com.welfare.service.operator.merchant.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 表示某种类型的MerchantAccountType操作了多少
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Data
public class MerchantAccountOperation {
    private MerCreditType type;
    private BigDecimal amount;
    private IncOrDecType incOrDecType;
    private MerchantBillDetail merchantBillDetail;
    private MerchantCredit merchantCredit;
    /**
     * 返回一个MerchantAccountOperation
     * @param operateType
     * @param operatedAmount
     * @param incOrDecType
     * @param merchantCredit
     * @param transNo
     * @param transType
     * @return
     */
    public static MerchantAccountOperation of(MerCreditType operateType,
                                              BigDecimal operatedAmount,
                                              IncOrDecType incOrDecType,
                                              MerchantCredit merchantCredit, String transNo, String transType){
        MerchantAccountOperation merchantAccountOperation = new MerchantAccountOperation();
        merchantAccountOperation.setType(operateType);
        merchantAccountOperation.setAmount(operatedAmount);
        merchantAccountOperation.setIncOrDecType(incOrDecType);

        MerchantBillDetail merchantBillDetail = new MerchantBillDetail();
        merchantBillDetail.setBalanceType(operateType.code());
        merchantBillDetail.setTransAmount(incOrDecType.equals(IncOrDecType.INCREASE) ? operatedAmount : operatedAmount.negate());
        merchantBillDetail.setTransType(transType);
        merchantBillDetail.setTransNo(transNo);
        merchantBillDetail.setMerCode(merchantCredit.getMerCode());
        merchantBillDetail.setCurrentBalance(merchantCredit.getCurrentBalance());
        merchantBillDetail.setRebateLimit(merchantCredit.getRebateLimit());
        merchantBillDetail.setCreditLimit(merchantCredit.getCreditLimit());
        merchantBillDetail.setRechargeLimit(merchantCredit.getRechargeLimit());
        merchantBillDetail.setRemainingLimit(merchantCredit.getRemainingLimit());
        merchantBillDetail.setSelfDepositBalance(merchantCredit.getSelfDepositBalance());
        merchantBillDetail.setWholesaleLimit(merchantCredit.getWholesaleCreditLimit());
        merchantBillDetail.setWholesaleRemainingLimit(merchantCredit.getWholesaleCredit());
        merchantAccountOperation.setMerchantBillDetail(merchantBillDetail);
        return merchantAccountOperation;
    }
    public static BigDecimal getCurrentBalanceOperated(List<MerchantAccountOperation> merchantAccountOperations){
        return merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                .filter(detail -> WelfareConstant.MerCreditType.CURRENT_BALANCE.code().equals(detail.getBalanceType()))
                .map(MerchantBillDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getRemainingLimitOperated(List<MerchantAccountOperation> merchantAccountOperations){
        return merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                .filter(detail -> WelfareConstant.MerCreditType.REMAINING_LIMIT.code().equals(detail.getBalanceType()))
                .map(MerchantBillDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
