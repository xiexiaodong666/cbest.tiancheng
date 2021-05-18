package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/30/2021
 */
@ApiModel("多订单联合退款明细")
@Data
public class MultiOrderRefundRequestDetail {
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("退款金额")
    private BigDecimal amount;
    @ApiModelProperty("退款日期")
    private Date refundDate;
    @ApiModelProperty("沃生活馆线上退款请求商品id集合")
    private List<String> saleUnIds;
}
