package com.welfare.common.exception;

/**
 *
 * @author liyx1
 */
public enum ExceptionCode {
    /**
     * 成功
     */
    SUCCESS(0),
    /**
     * 未知异常
     */
    UNKNOWON_EXCEPTION(0xffffffff),

    //----------系统异常-----------
    /**
     * 参数校验异常
     */
    ILLEGALITY_ARGURMENTS(0x00010000), //参数校验异常
    DATA_BASE_ERROR(0x00020000), //数据库访问异常

    //----------业务异常-----------
    BUSI_ERROR_LOGIN_FAILED(0x00000001),  //登录失败
    /**
     * 无权限
     */
    BUSI_ERROR_NO_PERMISSION(0x00000002),

    //---------资金相关异常----
    /**
     * 达到商户充值限额
     */
    MERCHANT_RECHARGE_LIMIT_EXCEED(0x00000101),



    //------条码相关异常------
    BARCODE_EXPIRE(0x00000201);






    int code;   //错误码， int的2进制 (前8位为服务编码)（后24位为异常编码（前8位为系统异常，后16位为业务异常））
    final int app = 0x01000000; //应用编码

    ExceptionCode(int code) {
        this.code = app | code;
    }

    public String getCode(){
        return String.valueOf(this.code);
    }

}
