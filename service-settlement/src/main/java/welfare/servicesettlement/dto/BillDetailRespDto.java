package welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细请求dto
 */
public class BillDetailRespDto {
    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @ApiModelProperty(value = "订单号")
    private String orderNO;

    @ApiModelProperty(value = "交易时间")
    private String transTime;

    @ApiModelProperty(value = "消费门店编号")
    private String storeCode;

    @ApiModelProperty(value = "消费门店名称")
    private String storeName;

    @ApiModelProperty(value = "商户编号")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "实际付款金额")
    private String payAmount;

    @ApiModelProperty(value = "退款金额")
    private String refundAmount;

    @ApiModelProperty(value = "实付金额")
    private String realAmount;

    @ApiModelProperty(value = "结算金额")
    private String settleAmount;
}
