package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * (商户账号类型)详情返回数据
 *
 * @author hao.yin
 * @since 2021-01-08 11:23:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypeDetailDTO {

    /**
     * 商户
     */
    @ApiModelProperty("商户")
	private String merName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    @ApiModelProperty("商户账户类型名称")
    private String merAccountTypeName;

    @ApiModelProperty("商户账户类型编码")
    private String merAccountTypeCode;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    private Date createTime;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    List<TypeItem> typeList;

    @Data
    @AllArgsConstructor
    public static class  TypeItem{
        @ApiModelProperty("扣款序号")
        private Integer deductionOrder;
        @ApiModelProperty("商户账户类型名称")
        private String merAccountTypeName;
    }

}