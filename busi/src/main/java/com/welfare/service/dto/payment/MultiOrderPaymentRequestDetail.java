package com.welfare.service.dto.payment;

import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRowsRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@ApiModel("多订单联合支付请求明细")
@Data
public class MultiOrderPaymentRequestDetail {

    @ApiModelProperty(value = "订单号",required = true)
    private String orderNo;

    @ApiModelProperty(value = "金额",required = true)
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty(value = "门店号",required = true)
    private String storeNo;

    @ApiModelProperty(value = "支付机器号（收银机或者虚拟收银机号）",required = true)
    private String machineNo;

    @ApiModelProperty("沃生活馆线上支付请求商品行数据,json格式 ["
            + "  {"
            + "    \"name\": \"重百线上商品 (注释:商品名称)\","
            + "    \"price\": 1 (注释:商品单价, 元),"
            + "    \"count\": 1 (注释:购买数量),"
            + "    \"saleUnId\": 123456789 (注释:商品id)"
            + "  }"
            + "]")
    private List<WoLifeAccountDeductionRowsRequest> saleRows;


}
