package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private Set<String> storeCodes;


    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
