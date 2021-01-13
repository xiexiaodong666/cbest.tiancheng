package com.welfare.common.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AccountRechargePaymentStatusEnum {
    PENDING_PAYMENT(100, "待支付"),
    PAYMENT_FAILURE(101, "支付失败"),
    PAYMENT_SUCCESS(102, "支付成功"),
    REFUND_FAILURE(103, "退款失败"),
    REFUND_SUCCESS(104, "退款成功"),
    QUERY_PAY_RESULT_NOT_FOUND(105, "超过一定时间未查询到支付结果");;

    private int code;
    private String name;

    AccountRechargePaymentStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final static Map<Integer, AccountRechargePaymentStatusEnum> ACCOUNT_RECHARGE_PAYMENT_STATUS_ENUM_MAP = Stream
        .of(AccountRechargePaymentStatusEnum.values()).collect(Collectors
            .toMap(AccountRechargePaymentStatusEnum::getCode,
                accountRechargePaymentStatusEnum -> accountRechargePaymentStatusEnum));

    public static AccountRechargePaymentStatusEnum getByCode(int code) {
        return ACCOUNT_RECHARGE_PAYMENT_STATUS_ENUM_MAP.get(code);
    }
}
