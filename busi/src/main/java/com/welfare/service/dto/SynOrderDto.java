package com.welfare.service.dto;

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
    @ApiModelProperty("订单号")
    private String orderId;
    @ApiModelProperty("交易流水号")
    private String getawayNo;
    @ApiModelProperty("退款流水号")
    private String returnGetawayNo;
    @ApiModelProperty("商品")
    private String goods;
    @ApiModelProperty("账户")
    private Long accountCode;
    @ApiModelProperty("卡号")
    private String cardId;
    @ApiModelProperty("消费门店")
    private String storeCode;
    @ApiModelProperty("消费类型")
    private String transType;
    @ApiModelProperty("消费金额")
    private BigDecimal transAmount;
    @ApiModelProperty("消费时间")
    private Date transTime;
}
