package com.welfare.service.operator.payment.domain;

import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/16/2021
 */
@Data
public class RefundOperation {
    private AccountBillDetail refundBillDetail;
    private AccountDeductionDetail refundDeductionDetail;

    public static RefundOperation of(AccountBillDetail refundBillDetail,AccountDeductionDetail refundDeductionDetail){
        RefundOperation refundOperation = new RefundOperation();
        refundOperation.setRefundBillDetail(refundBillDetail);
        refundOperation.setRefundDeductionDetail(refundDeductionDetail);
        return refundOperation;
    }
}
