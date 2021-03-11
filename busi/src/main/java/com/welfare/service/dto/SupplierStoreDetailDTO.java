package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreDetailDTO {

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
     * 商户代码
     */
    @ApiModelProperty("商户名称")
    private String merName;

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
     * 父级门店
     */
    @ApiModelProperty("父级门店")  
    private String storeParent;
    /**
     * 备注
     */
    @ApiModelProperty("备注")  
    private String remark;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式(前端多个用逗号分隔)")
    private String consumType;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式转义")
    private String consumTypeName;

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
     * 更新人
     */
    @ApiModelProperty("更新人")  
    private String updateUser;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")  
	private Date updateTime;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")  
    private String externalCode;

    List<MerchantAddressDTO> addressList;

    /**
     * 门店关联消费方法虚拟收银号
     */
    @ApiModelProperty("门店关联消费方法虚拟收银号")
    List<StoreConsumeTypeDTO> storeConsumeTypeList;

    /**
     * 门店手机号
     */
    @ApiModelProperty("门店手机号")
    private String mobile;
}