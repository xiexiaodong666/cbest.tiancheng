package com.welfare.service;

import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.MultiOrderRefundRequest;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/15/2021
 */
public interface RefundService {
    /**
     * 处理退款请求
     * @param refundRequest 退款请求
     */
    void handleRefundRequest(RefundRequest refundRequest);

    /**
     * 查询退款结果
     * @param transNo 交易单号
     * @return 退款请求结果
     */
    RefundRequest queryResult(String transNo);

    /**
     * 多订单联合退款
     * @param refundRequest 多订单退款请求
     */
    void multiOrderRefund(MultiOrderRefundRequest refundRequest);
}
