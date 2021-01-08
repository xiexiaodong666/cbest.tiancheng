package com.welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ProjectName: e-welfare
 * @Package: welfare.servicesettlement.dto
 * @ClassName: OrderReqDto
 * @Author: jian.zhou
 * @Description: 订单
 * @Date: 2021/1/7 20:28
 * @Version: 1.0
 */
@ApiModel("订单请求实体")
@Data
public class OrderReqDto implements Serializable {

    @ApiModelProperty(value = "订单号" , required = false)
    private String orderId;
    @ApiModelProperty(value = "卖家名称" , required = false)
    private String consumerName;
    @ApiModelProperty(value = "最低价格" , required = false)
    private BigDecimal lowPrice;
    @ApiModelProperty(value = "最高价格" , required = false)
    private BigDecimal hightPrice;
    @ApiModelProperty(value = "门店类型" , required = false)
    private String storeType;
    @ApiModelProperty(value = "开始时间" , required = false)
    private String startDateTime;
    @ApiModelProperty(value = "结束时间" , required = false)
    private String endDateTime;
    @ApiModelProperty(value = "门店编码" , required = false)
    private String storeCode;
    @ApiModelProperty(value = "页面" , required = true)
    private Integer pageNo;
    @ApiModelProperty(value = "页数据" , required = true)
    private Integer pageSize;
}
