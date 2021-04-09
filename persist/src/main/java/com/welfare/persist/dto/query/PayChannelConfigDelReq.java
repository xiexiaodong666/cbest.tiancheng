package com.welfare.persist.dto.query;

import lombok.Data;

/**
 * @author duanhy
 * @title: PayChannelConfigDelReq
 * @description: TODO
 * @date 2021/3/2817:15
 */
@Data
public class PayChannelConfigDelReq {

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
