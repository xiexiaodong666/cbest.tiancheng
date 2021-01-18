package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (商户账号类型)分页返回数据
 *
 * @author hao.yin
 * @since 2021-01-08 11:23:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@ExcelIgnoreUnannotated
public class MerchantAccountTypeWithMerchantDTO {
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;
    /**
     * 商户
     */
    @ApiModelProperty("商户")
    @ExcelProperty(value = "商户",index = 0)
    private String merName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    @ExcelProperty(value = "商户代码",index =1)
    private String merCode;
    /**
     * 商户账户类型数
     */
    @ApiModelProperty("商户账户类型数量")
    @ExcelProperty(value = "商户账户类型数量",index =2)
    private Integer count;

    /**
     * 商户账户类型名称
     */
    @ApiModelProperty("商户账户类型名称")
    @ExcelProperty(value = "福利类型",index =3)
    private String merAccountTypeName;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @ExcelProperty(value = "创建时间",index =4)
    private Date createTime;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @ExcelProperty(value = "创建时间",index =5)
    private String remark;


}