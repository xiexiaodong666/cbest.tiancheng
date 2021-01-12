package com.welfare.common.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AccountPayTypeEnum {
    WX("wx", "微信"),
    ALIPAY("alipay", "支付宝");

    private String type;
    private String name;


    AccountPayTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private final static Map<String, AccountPayTypeEnum> ENUM_MAP = Stream
        .of(AccountPayTypeEnum.values()).collect(Collectors
            .toMap(AccountPayTypeEnum::getType,
                e -> e));

    public static AccountPayTypeEnum getByType(String type) {
        return ENUM_MAP.get(type);
    }
}
