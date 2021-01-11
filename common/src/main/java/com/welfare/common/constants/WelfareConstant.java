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
    private WelfareConstant(){

    }

    public enum PaymentScene {
        /**
         * 线下供应商
         */
        OFFLINE_SUPPLIER("OFFLINE_SUPPLIER","线下供应商"),
        /**
         * 线下重百
         */
        OFFLINE_CBEST("OFFLINE_CBEST","线下重百"),
        /**
         * 线上商城
         */
        ONLINE_STORE("ONLINE_STORE","线上商城");

        private String code;
        private String desc;

        PaymentScene(String code, String desc) {
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

    public enum Header{
        SOURCE("Source","请求来源"),
        MERCHANT_USER("merchantUser","商户请求用户"),
        API_USER("apiUser","平台api请求用户");

        private String code;
        private String desc;

        Header(String code,String desc){
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
     * 充值来源，用以标记
     */
    public enum MerCreditType {
        /**
         *
         */
        RECHARGE_LIMIT("rechargeLimit","充值额度"),
        CURRENT_BALANCE("currentBalance","目前余额"),
        CREDIT_LIMIT("creditLimit","信用额度"),
        REMAINING_LIMIT("remainingLimit","剩余信用额度"),
        REBATE_LIMIT("rebateLimit","返利余额");

        MerCreditType(String code,String desc){
            this.code = code;
            this.desc = desc;
        }

        private String code;
        private String desc;

        public String code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }

        public static MerCreditType findByCode(String code){
            for (MerCreditType type : MerCreditType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new RuntimeException("不存在的MerCreditType类型");
        }
    }
}
