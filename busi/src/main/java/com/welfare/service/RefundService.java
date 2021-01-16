package com.welfare.service;

import com.welfare.service.dto.RefundRequest;

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
     * @param refundRequest
     * @return
     */
    void handleRefundRequest(RefundRequest refundRequest);

    /**
     * 查询退款结果
     * @param transNo
     * @return
     */
    RefundRequest queryByTransNo(String transNo);
}
