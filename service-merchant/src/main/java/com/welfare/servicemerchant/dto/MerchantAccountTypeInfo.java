package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * (商户福利类型)实体类
 *
 * @author hao.yin
 * @since 2021-01-08 11:23:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypeInfo {

    /**
     * 自增id
     */
    @ApiModelProperty("自增id")
	private Integer id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 商户账户类型编码
     */
    @ApiModelProperty("商户账户类型编码")   
    private String merAccountTypeCode;
    /**
     * 商户账户类型名称
     */
    @ApiModelProperty("商户账户类型名称")   
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
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
	private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private Integer version;


}