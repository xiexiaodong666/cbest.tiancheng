package com.welfare.common.exception;

import net.dreamlu.mica.core.result.IResultCode;

/**
 *
 * @author liyx1
 */
public enum ExceptionCode implements IResultCode {
    /**
     * 成功
     */
    SUCCESS(200,"成功"),
    /**
     * 未知异常
     */
    UNKNOWON_EXCEPTION(5000000,"未知异常"),

    //----------系统异常-----------
    ILLEGALITY_ARGURMENTS(5001001,"参数校验异常"),
    DATA_BASE_ERROR(5001002,"数据库异常"),

    //----------业务异常-----------

    //---------资金相关异常----
    MERCHANT_RECHARGE_LIMIT_EXCEED(5002001,"商户余额不足"),
    INSUFFICIENT_BALANCE(5002002,"用户余额不足"),


    //------条码相关异常------
    BARCODE_EXPIRE(5003001,"条码过期"),

    //______卡片相关异常------
    CARD_WRITTEN_OR_BIND(5004001,"卡片已被写入或者绑定, 不能删除");





    private int code;
    private String msg;

    ExceptionCode(int code,String msg) {
        this.code =  code;
        this.msg = msg;
    }

    @Override
    public int getCode(){
        return this.code;
    }
    @Override
    public String getMsg(){
        return this.msg;
    }
}
