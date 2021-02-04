package com.welfare.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.servicesettlement.dto
 * @ClassName: SynOrderDto
 * @Author: jian.zhou
 * @Description:
 * @Date: 2021/1/14 22:41
 * @Version: 1.0
 */
@ApiModel("订单同步实体")
@Data
public class SynOrderDto {
    @ApiModelProperty(value = "订单号" , required = true)
    private String orderId;
    @ApiModelProperty(value = "交易流水号" , required = true)
    private String transNo;
    @ApiModelProperty(value = "退款流水号" , required = false)
    private String returnTransNo;
    @ApiModelProperty(value = "商品" , required = false)
    private String goods;
    @ApiModelProperty(value = "账户" , required = true)
    private Long accountCode;
    @ApiModelProperty(value = "卡号" , required = false)
    private String cardId;
    @ApiModelProperty(value = "门店号" , required = true)
    private String storeCode;
    @ApiModelProperty(value = "交易类型[consume(消费)|refund(退款)]" , required = true)
    private String transType;
    @ApiModelProperty(value = "交易金额" , required = true)
    private BigDecimal transAmount;
    @ApiModelProperty(value = "交易时间[yyyy-MM-dd HH:mm:ss]" , required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date transTime;
    @ApiModelProperty(value = "消费时段" , required = false)
    private Integer timeInterval;
}
