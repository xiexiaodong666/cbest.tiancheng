package com.welfare.service.remote.entity;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/19 11:46 上午
 * @desc
 */
@Data
public class MerchantCreditResp {
    /**
     * 1-成功 -1失败
     */
    private int code;

    private boolean success;

    private String msg;

    private T data;
}
