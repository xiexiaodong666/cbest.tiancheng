package com.welfare.service.dto;

import com.welfare.common.base.RequestPage;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细响应dto
 */
@Data
public class MonthSettleDetailPageReq extends PageReq {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编号")
    private String storeCode;

    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "福利类型")
    private String welfareTypeCode;

    @ApiModelProperty(value = "门店类型 自营：self，第三方：third")
    private String storeType;
}
