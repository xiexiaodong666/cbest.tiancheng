package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreAddDTO {

    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    @NotBlank
    private String merCode;
    /**
     * 门店代码
     */
    @ApiModelProperty("门店代码")
    @NotBlank
    @Length(max = 4, min = 4)
    @Pattern(regexp = "^[0-9A-Z]+$")
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    @NotBlank
    private String storeName;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Length(max = 50)
    private String remark;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式(前端多个用逗号分隔)")
    @NotBlank
    private String consumType;

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
    @ApiModelProperty("手机号（11位)")
    @Size(max = 12, min = 8, message = "手机号不合法")
    @Pattern(regexp = "^-?[0-9]+$", message = "手机号不合法")
    private String mobile;
}