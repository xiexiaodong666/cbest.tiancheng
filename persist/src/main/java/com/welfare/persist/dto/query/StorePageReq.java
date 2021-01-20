package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class StorePageReq {
    @ApiModelProperty("商户代码")
    private String merCode;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店状态")
    private Integer status;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("门店编码集合")
    private List<String> storeCodes;
}
