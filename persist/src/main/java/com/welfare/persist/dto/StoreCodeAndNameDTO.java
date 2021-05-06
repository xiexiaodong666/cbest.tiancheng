package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/5/6 2:07 下午
 */
@Data
public class StoreCodeAndNameDTO {

    @ApiModelProperty("编码")
    private String storeCode;
    @ApiModelProperty("名称")
    private String storeName;
}
