package com.welfare.common.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AccountRechargeStatusEnum {
    PENDING_RECHARGE(200, "待充值"),
    RECHARGE_PROCESSING(201, "充值处理中"),
    RECHARGE_FAILURE(202, "充值失败"),
    RECHARGE_SUCCESS(203, "充值成功");

    private int code;
    private String name;

    AccountRechargeStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final static Map<Integer, AccountRechargeStatusEnum> ACCOUNT_RECHARGE_STATUS_ENUM_MAP = Stream
        .of(AccountRechargeStatusEnum.values()).collect(Collectors
            .toMap(AccountRechargeStatusEnum::getCode,
                accountRechargeStatusEnum -> accountRechargeStatusEnum));

    public static AccountRechargeStatusEnum getByCode(int code) {
        return ACCOUNT_RECHARGE_STATUS_ENUM_MAP.get(code);
    }
}
