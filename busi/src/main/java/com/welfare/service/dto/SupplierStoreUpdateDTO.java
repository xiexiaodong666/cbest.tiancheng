package com.welfare.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreUpdateDTO {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id")
	private Long id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    @NotBlank
    private String merCode;
    @ApiModelProperty("虚拟收银机号")
    private String cashierNo;
    /**
     * 门店代码
     */
    @ApiModelProperty("门店代码")
    @NotBlank
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
    private String remark;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式(前端多个用逗号分隔)")
    @NotBlank
    private String consumType;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @NotBlank
    private String updateUser;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")  
    private String externalCode;

    List<MerchantAddressDTO> addressList;

}