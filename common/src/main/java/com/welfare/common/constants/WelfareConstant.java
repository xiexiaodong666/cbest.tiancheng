package com.welfare.common.constants;

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
    public enum DictType {
        /**
         * 门店消费类型
         */
        STORE_CONSUM_TYPE("SupplierStore.consumType", "消费类型");

        private final String code;
        private final String desc;

        DictType(String code, String desc) {
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

        private final String code;
        private final String desc;

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
        API_USER("apiUser", "平台api请求用户"),
        ACCOUNT_USER("accountUser", "员工请求用户");

        private final String code;
        private final String desc;

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
        SELF_DEPOSIT("self_deposit","自主充值"),
        CREDIT_LIMIT("creditLimit", "信用额度"),
        REMAINING_LIMIT("remainingLimit", "剩余信用额度"),
        REBATE_LIMIT("rebateLimit", "返利余额");

        MerCreditType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private final String code;
        private final String desc;

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
        ACCOUNT_TYPE_CODE("account_type_code","员工类型编码"),
        ACCOUNT_CODE("account_code","员工账号"),
        DEPARTMENT_CODE("department_code","部门编号"),
        MER_CODE("mer_code","商户编号"),
        ACCOUNT_DEPOSIT_APPLY("account_deposit_apply", "员工账号福利余额变更申请");
        private final String code;
        private final String desc;

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
        private final String code;
        private final String desc;

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
        DEPOSIT_INCR("deposit_incr","充值(增加)"),
        DEPOSIT_DECR("deposit_decr","充值(减少)"),
        REFUND("refund","退款"),
        RESET_INCR("reset_incr","设置（增加)"),
        RESET_DECR("reset_decr","设置（减少)"),
        REBATE_DECR("rebate_decr","返点（减少)"),
        REBATE_INCR("rebate_incr","返点（新增)");
        private final String code;
        private final String desc;

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
     * 支付类型,用于结算
     */
    public enum PayCode{
        /**
         * 账户类型
         */
        WELFARE_CARD("EMPLOYEE_CARD_NUMBER","福利卡消费"),
        OTHER("OTHER_PAY","其他消费");
        private final String code;
        private final String desc;

        PayCode(String code, String desc) {
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
        NEW(0,"新增"),HANDLING(1,"处理中"),SUCCEED(2,"成功"),FAILED(-1,"失败"),REVERSED(3,"已逆向操作");

        private final Integer code;
        private final String desc;

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


        private final String code;
        private final String desc;

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
        WRITTEN(1,"已写入"),
        BIND(2,"已绑定");
        private final Integer code;
        private final String desc;

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

        private final String code;
        private final String desc;

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
