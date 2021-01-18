package com.welfare.common.constants;

import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.result.Result;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/9 4:15 下午
 * @desc
 */
public class WelfareSettleConstant {

    /**
     * 账单状态枚举
     */
    public enum SettleRecStatusEnum {
        /**
         * 待确认
         */
        UNCONFIRMED("unconfirmed","待确认"),
        /**
         * 已确认
         */
        CONFIRMED("confirmed","已确认");

        private String code;
        private String desc;

        SettleRecStatusEnum(String code, String desc) {
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
     * 发送状态枚举
     */
    public enum SettleSendStatusEnum {
        /**
         * 待发送
         */
        UNSENDED("unsended","待发送"),

        /**
         * 已发送
         */
        SENDED("sended","已发送");

        private String code;
        private String desc;

        SettleSendStatusEnum(String code, String desc) {
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
     * 结算状态枚举
     */
    public enum SettleStatusEnum {
        /**
         * 待结算
         */
        UNSETTLED("unsettled","待结算"),

        /**
         * 已结算
         */
        SETTLED("settled","已结算");

        private String code;
        private String desc;

        SettleStatusEnum(String code, String desc) {
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
     * 发送状态枚举
     */
    public enum PullTaksSendStatusEnum {
        /**
         * 成功
         */
        SUCCESS("success","成功"),

        /**
         * 失败
         */
        FAIL("fail","失败");

        private String code;
        private String desc;

        PullTaksSendStatusEnum(String code, String desc) {
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
     * 结算明细数据类型枚举
     */
    public enum SettleDetailDataTypeEnum {
        /**
         * 员工卡支付
         */
        WELFARE("welfare","员工卡支付"),

        /**
         * 第三方支付
         */
        THIRD("third","第三方支付");

        private String code;
        private String desc;

        SettleDetailDataTypeEnum(String code, String desc) {
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


    public static final Integer LIMIT = 2000;
}
