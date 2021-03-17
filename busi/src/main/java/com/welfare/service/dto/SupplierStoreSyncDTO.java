package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreSyncDTO {
    private Long id;

    private Integer status;

    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")  
    private String merCode;
    @ApiModelProperty("虚拟收银机号")
    private String cashierNo;
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
     * 消费方式
     */
    @ApiModelProperty("消费方式(前端多个用逗号分隔)")
    private String consumType;
    /**
     * 门店手机号
     */
    @ApiModelProperty("门店手机号")
    private String mobile;

    List<MerchantAddressDTO> addressList;
    List<StoreConsumeTypeDTO> storeConsumeTypeList;

}