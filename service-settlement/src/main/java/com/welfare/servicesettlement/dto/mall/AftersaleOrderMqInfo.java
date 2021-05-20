package com.welfare.servicesettlement.dto.mall;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderInfoDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@ApiModel
@Data
public class AftersaleOrderMqInfo extends OrderMqInfo{
    @ApiModelProperty("售后订单ID")
    private Long aftersaleId;

    @ApiModelProperty("售后订单号")
    private Long aftersaleNo;

    @ApiModelProperty("售后类型(1:退款 2:退货 3:换货)")
    private Integer aftersaleType;

    @ApiModelProperty(value = "售后状态码")
    private Integer aftersaleStatusCode;

    @ApiModelProperty(value = "售后状态名称")
    private String aftersaleStatusName;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("退货原因")
    private String applyReason;

    @ApiModelProperty("退货原因 1:商品变质不新鲜 2:商品漏发 3:商家发错货 4:商品信息描述不符 5:其他")
    private Integer applyType;

    @ApiModelProperty("退货原因备注")
    private String aftersaleRemark;

    @ApiModelProperty("售后订单创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    @ApiModelProperty("退款时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtRefundPay;

    @ApiModelProperty("售后申请人ID")
    private Long applyUserId;

    @ApiModelProperty("售后申请人姓名")
    private String applyUserName;

    @ApiModelProperty("售后申请人电话")
    private String applyUserPhone;

    private BigDecimal refundWholesaleAmount;

    public OrderInfo parseFromOriginalOrder(OrderInfo originalOrderInfo){
        OrderInfo afterSaleOrderInfo = new OrderInfo();
        BeanUtils.copyProperties(originalOrderInfo,afterSaleOrderInfo);
        afterSaleOrderInfo.setOrderId(this.getOrgOrderNo().toString());
        afterSaleOrderInfo.setOrderAmount(this.refundAmount);
        afterSaleOrderInfo.setTransNo(super.getTradeNo());
        afterSaleOrderInfo.setReturnTransNo(originalOrderInfo.getTransNo());
        afterSaleOrderInfo.setOrderTime(this.getOrderTime());
        afterSaleOrderInfo.setTransType(WelfareConstant.TransType.REFUND.code());
        afterSaleOrderInfo.setTransTypeName(WelfareConstant.TransType.REFUND.desc());
        //需要insert，id置空
        afterSaleOrderInfo.setId(null);
        return afterSaleOrderInfo;
    }

    @Override
    public BigDecimal computeTransAmount(OrderDetailMqInfo orderDetailMqInfo){
        return orderDetailMqInfo.getRefundAmount();
    }
}
