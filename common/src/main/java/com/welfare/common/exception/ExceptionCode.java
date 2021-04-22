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

    //----------业务异常------------------------------------------------------------------------------------------------


    //---------资金相关异常----
    MERCHANT_RECHARGE_LIMIT_EXCEED(5002001,"商户余额不足"),
    INSUFFICIENT_BALANCE(5002002,"用户余额不足"),
    REFUND_MORE_THAN_PAID(5002003,"累计退款金额不能大于付款金额"),

    //------条码相关异常------
    BARCODE_EXPIRE(5003001,"条码过期"),

    //______卡片相关异常------
    CARD_WRITTEN_OR_BIND(5004001,"卡片已被写入或者绑定, 不能删除"),
    CARD_ALREADY_BIND(5004002,"卡片已经被绑定其他用户"),
    ACCOUNT_ALREADY_BIND(5004003,"用户已经绑定卡"),
    //______授权相关异常------
    BUSI_ERROR_NO_PERMISSION(5005001,"未登录"),

    //______数据相关异常------
    DATA_NOT_EXIST(50060001,"数据不存在"),
    //______配置相关异常------
    NO_AVAILABLE_AMOUNT_TYPE(50070001,"当前场景没有可用的福利类型"),

    //______账户、员工相关异常------
    ACCOUNT_EXIST(50080001,"员工已存在"),
    ACCOUNT_DISABLED(5008002,"账户已禁用"),
    ACCOUNT_NOT_EXIST(5008003,"账户不存在");


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
