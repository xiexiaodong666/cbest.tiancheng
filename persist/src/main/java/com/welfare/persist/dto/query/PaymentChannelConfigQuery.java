package com.welfare.persist.dto.query;

import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/5/7 9:43 上午
 */
@Data
public class PaymentChannelConfigQuery {

    /**
     * 商户编码
     */
    private String merCode;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 消费方式
     */
    private String consumeType;
}
