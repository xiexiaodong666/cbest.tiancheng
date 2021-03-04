package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/4/2021
 */
@Data
@ApiModel("门店编码名称")
public class StoreCodeNameDTO implements Serializable {
    @ApiModelProperty("编码")
    private String storeCode;
    @ApiModelProperty("名称")
    private String storeName;
}
