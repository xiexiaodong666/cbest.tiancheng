package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 供应商门店导入类
 *
 * @author hao.yin
 * @since 2021-01-12 11:52:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@ApiModel("商户门店")
public class SupplierStoreImportDTO {
    /**
     * 门店代码
     */
    @ApiModelProperty("门店代码")
    @ExcelProperty(value = "门店代码", index = 0)
    private String storeCode;

    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    @ExcelProperty(value = "门店名称", index = 1)
    private String storeName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    @ExcelProperty(value = "所属商户代码", index = 2)
    private String merCode;
    /**
     * 门店消费类型
     */
    @ApiModelProperty("门店消费类型")
    @ExcelProperty(value = "门店消费类型", index =3)
    private String consumType;
    /**
     * O2O虚拟收银机号
     */
    @ApiModelProperty("O2O虚拟收银机号")
    @ExcelProperty(value = "虚拟收银机号", index = 4)
    private String o2oCashierNo;

    /**
     * 线上商城虚拟收银机号
     */
    @ApiModelProperty("线上商城虚拟收银机号")
    @ExcelProperty(value = "线上商城虚拟收银机号", index = 5)
    private String onlineCashierNo;

    /**
     * 批发商城虚拟收银机号
     */
    @ApiModelProperty("批发商城虚拟收银机号")
    @ExcelProperty(value = "批发商城虚拟收银机号", index = 6)
    private String wholesaleCashierNo;
    /**
     * 自提点名称
     */
    @ApiModelProperty("自提点名称")
    @ExcelProperty(value = "自提点名称", index = 7)
    private String addressName;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    @ExcelProperty(value = "详细地址", index = 8)
    private String address;





}