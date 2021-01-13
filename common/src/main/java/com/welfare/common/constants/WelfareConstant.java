package com.welfare.common.constants;

import lombok.EqualsAndHashCode;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
public class WelfareConstant {
    private WelfareConstant() {

    }

    public enum PaymentScene {
        /**
         * 线下供应商
         */
        OFFLINE_SUPPLIER("OFFLINE_SUPPLIER", "线下供应商"),
        /**
         * 线下重百
         */
        OFFLINE_CBEST("OFFLINE_CBEST", "线下重百"),
        /**
         * 线上商城
         */
        ONLINE_STORE("ONLINE_STORE", "线上商城");

        private String code;
        private String desc;

        PaymentScene(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }
    }

    public enum Header {
        /**
         * 请求头的KEY
         */
        SOURCE("Source", "请求来源"),
        MERCHANT_USER("merchantUser", "商户请求用户"),
        API_USER("apiUser", "平台api请求用户");

        private String code;
        private String desc;

        Header(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }
    }

    /**
     * 充值来源，用以标记
     */
    public enum MerCreditType {
        /**
         *
         */
        RECHARGE_LIMIT("rechargeLimit", "充值额度"),
        CURRENT_BALANCE("currentBalance", "目前余额"),
        CREDIT_LIMIT("creditLimit", "信用额度"),
        REMAINING_LIMIT("remainingLimit", "剩余信用额度"),
        REBATE_LIMIT("rebateLimit", "返利余额");

        MerCreditType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private String code;
        private String desc;

        public String code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }

        public static MerCreditType findByCode(String code) {
            for (MerCreditType type : MerCreditType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new RuntimeException("不存在的MerCreditType类型");
        }
    }

    /**
     * 序列号类型
     */
    public enum SequenceType {
        /**
         * 序列号类型
         */
        MERCHANT_CREDIT_APPLY("merchant_credit_apply", "商户额度变更申请"),
        DEPOSIT("deposit","账户充值"),
        CARD_NO("cardNo","卡号"),
        MER_ACCOUNT_TYPE_CODE("mer_account_type","福利类型编号"),
        DEPARTMENT_CODE("department_code","部门编号"),
        MER_CODE("mer_code","商户编号");
        private String code;
        private String desc;

        SequenceType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }

    /**
     * 账户类型
     */
    public enum MerAccountTypeCode{
        /**
         * 账户类型
         */
        SELF("self","自主余额"),
        SURPLUS_QUOTA("surplus_quota","授信额度");
        private String code;
        private String desc;

        MerAccountTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }

    /**
     * 账户类型
     */
    public enum TransType{
        /**
         * 账户类型
         */
        CONSUME("consume","消费"),
        DEPOSIT("deposit","充值"),
        REFUND("refund","退款");
        private String code;
        private String desc;

        TransType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }

    /**
     * 异步状态
     */
    public enum AsyncStatus {
        /**
         *  异步状态
         */
        NEW(0,"新增"),HANDLING(1,"处理中"),SUCCEED(2,"成功"),FAILED(-1,"失败");

        private Integer code;
        private String desc;

        AsyncStatus(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }

        public Integer code(){
            return this.code;
        }

        public String desc(){
            return this.desc;
        }
    }

    public enum HeaderSource{
        /**
         * header.Source的取值
         */
        MERCHANT_BACKEND("merchant_backend","甜橙生活商户后台"),
        CBEST_PAY("cbest_pay","重百付"),
        E_WELFARE_ACCOUNT("e_welfare_account","福利平台-账户服务"),
        E_WELFARE_MERCHANT("e_welfare_merchant","福利平台-商户服务"),
        E_WELFARE_SETTLEMENT("e_welfare_settlement","福利平台-结算服务"),
        E_WELFARE_API("e_welfare_api","福利平台-api"),
        CARD_INFO_WRITER("card_info_writer","写卡客户端");


        private String code;
        private String desc;

        HeaderSource(String code, String desc){
            this.code = code;
            this.desc = desc;
        }

        public String code(){
            return this.code;
        }

        public String desc(){
            return this.desc;
        }
    }

    /**
     * 卡状态
     */
    public enum CardStatus{

        /**
         * 卡状态
         */
        NEW(0,"新增"),
        WRITTEN(1,"已写入");

        private Integer code;
        private String desc;

        CardStatus(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }

        public Integer code(){
            return this.code;
        }

        public String desc(){
            return this.desc;
        }
    }

    /**
     * 渠道
     */
    public enum Channel {
        /**
         * 请求头的KEY
         */
        ALIPAY("alipay", "支付宝"),
        WECHAT("wechat", "微信"),
        PLATFORM("platform", "平台");

        private String code;
        private String desc;

        Channel(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }
    }
}
