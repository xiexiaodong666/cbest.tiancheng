package com.welfare.service.payment;

import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.service.dto.RefundRequest;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public interface IRefundOperator {
    /**
     * 退款
     * @param refundRequest
     * @param refundDeductionInDbs
     * @param paidDeductionDetails
     * @param accountCode
     */
    void refund(RefundRequest refundRequest,
                List<AccountDeductionDetail> refundDeductionInDbs,
                List<AccountDeductionDetail> paidDeductionDetails,
                Long accountCode);
}
