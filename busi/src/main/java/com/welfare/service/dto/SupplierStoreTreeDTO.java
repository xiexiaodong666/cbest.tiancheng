package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreTreeDTO extends Tree{

    /**
     * id
     */
    @ApiModelProperty("id")
	private Long id;

    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")  
    private String merCode;
    /**
     * 门店代码
     */
    @ApiModelProperty("门店代码")  
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")  
    private String storeName;
    /**
     * 门店层级
     */
    @ApiModelProperty("门店层级")  
    private Integer storeLevel;
    /**
     * 父级门店
     */
    @ApiModelProperty("父级门店")  
    private String storeParent;
    /**
     * 门店路径
     */
    @ApiModelProperty("门店路径")  
    private String storePath;
    /**
     * 备注
     */
    @ApiModelProperty("备注")  
    private String remark;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式")  
    private String consumType;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")  
    private String createUser;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")  
    private Date createTime;

    /**
     * disabled false 门店 true 商户
     */
    @ApiModelProperty("type 1商户，2门店")
    private Boolean isMerchant;

}