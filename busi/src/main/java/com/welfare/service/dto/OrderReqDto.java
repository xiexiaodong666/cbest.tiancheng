package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
    @ApiModelProperty(value = "商户编码" , required = false)
    private String merchantCode;
    @ApiModelProperty(value = "供应商编码" , required = false)
    private String supplierMerchantCode;
    @ApiModelProperty(value = "开始时间" , required = false)
    private String startDateTime;
    @ApiModelProperty(value = "结束时间" , required = false)
    private String endDateTime;
    @ApiModelProperty(value = "门店编码" , required = false)
    private List<String> storeIds;
    @ApiModelProperty(value = "页面" , required = false)
    private Integer current;
    @ApiModelProperty(value = "页数据" , required = false)
    private Integer size;
    @ApiModelProperty(value = "调用方[商户端:MERCHANT , 供应商：SUPPLIER , 平台端: PLATFORM]" , required = false)
    private String type;
    @ApiModelProperty(value = "组织编码" , required = false)
    private String organizationCode;
    @ApiModelProperty(value = "用餐时段" , required = false)
    private Integer timeInterval;
    @ApiModelProperty(value = "账户类别" , required = false)
    private String accountType;
}
