package com.welfare.common.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DepartmentTypeEnum {
    DISTRIBUTION_CENTER("DISTRIBUTION_CENTER", "配送中心","1"),
    DEPARTMENT("DEPARTMENT", "部门","2"),
    GROUP("GROUP","小组","3");

    private String type;
    private String name;
    //导入excel的时候传的code
    private String excelType;



    DepartmentTypeEnum(String type, String name,String excelType) {
        this.type = type;
        this.name = name;
        this.excelType=excelType;
    }

    public String getType() {
        return type;
    }

    public String getExcelType() {
        return excelType;
    }

    public String getName() {
        return name;
    }

    private final static Map<String, String> TYPE_MAP = Stream
            .of(DepartmentTypeEnum.values()).collect(Collectors
                    .toMap(DepartmentTypeEnum::getExcelType,
                            DepartmentTypeEnum::getType));


    public static String getTypeByExcelType(String excelType) {
        return TYPE_MAP.get(excelType);
    }

}
