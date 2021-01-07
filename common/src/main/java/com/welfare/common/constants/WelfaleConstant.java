package com.welfare.common.constants;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
public class WelfaleConstant {
    private WelfaleConstant(){

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
        SOURCE("Source","请求来源");

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
}
