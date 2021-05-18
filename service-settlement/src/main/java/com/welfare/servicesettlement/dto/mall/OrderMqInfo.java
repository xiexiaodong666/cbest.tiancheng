package com.welfare.servicesettlement.dto.mall;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@Data
public class OrderMqInfo implements Serializable {
    private static final long serialVersionUID = -1487457198472623355L;
    private Integer lastStatusCode;
    private BigDecimal refundAmount;
    private BigDecimal supplierRefundAmount;
    private Date refundTime;
    private List<Long> cartIds;
    private Map<String, Integer> productInfo;
    private Long id;
    private Date orderDate;
    private Date orderTime;
    private Integer orderType;
    private Long orgOrderNo;
    private String orgThirdNo;
    private Long storeId;
    private String storeCode;
    private String storeName;
    private Integer payType;
    private Date payTime;
    private Integer isDelivery;
    private String orderIp;
    private String deviceId;
    private Integer statusCode;
    private String statusName;
    private Integer aftersaleStatusCode;
    private String aftersaleStatusName;
    private Long memberId;
    private String memberName;
    private String memberPhone;
    private String receiveName;
    private String receiveAddr;
    private String receiveAddrLongitude;
    private String receiveAddrLatitude;
    private String nickname;
    private String receivePhone;
    private BigDecimal orderAmount;
    private BigDecimal saleAmount;
    private BigDecimal originalAmount;
    private BigDecimal supplierDiscountAmount;
    private BigDecimal discountAmount;
    private BigDecimal purchaseAmount;
    private BigDecimal packageAmount;
    private BigDecimal fare;
    private BigDecimal thirdPayDiscountAmount;
    private BigDecimal orderRefundAmount;
    private Integer orderWeight;
    private Integer orderCount;
    private Long supplierId;
    private String supplierName;
    private Date expectReceiveDate;
    private Date expectReceiveStartTime;
    private Date expectReceiveEndTime;
    private Date receiveTime;
    private Integer deliveredTimes;
    private String remark;
    private Integer closeType;
    private Long sessionId;
    private String channelSource;
    private String mealCode;
    private String deliveryCode;
    private Integer systemSrc;
    private Integer isComment;
    private Integer isDelete;
    private Integer isReturnFreight;
    private Integer isHasBulk;
    private Integer isAftersale;
    private Integer isAftersaleRefund;
    private Integer termOfValidity;
    private Integer isBill;
    private Integer isExchange;
    private String aftersaleSupplierOperators;
    private String aftersalePlatformOperators;
    /**
     *     机具主扫token
     */
    private String token;
    /**
     * // 机具主扫核销码
     */
    private String verifyCode;
    /**
     * // 机具主扫支付二维码编号
     */
    private String qrcodeNo;
    private Date operTime;
    private Integer isPickUp;
    private String partnerCode;
    private String partnerName;
    private String departmentCode;
    private String departmentName;
    private String merchantRole;
    private String merchantRoleName;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
    @ApiModelProperty(value = "消费方式")
    private String consumeType;
    @ApiModelProperty("订单结算金额")
    private BigDecimal orderWholesaleAmount;

    @ApiModelProperty("商品结算金额")
    private BigDecimal productWholesaleAmount;

    @ApiModelProperty("重百付交易单号")
    private String tradeNo;

    private List<OrderDetailMqInfo> orderDetails;

    public static OrderInfo  parseToOrderInfo(OrderMqInfo orderMqInfo, AccountDeductionDetail accountDeductionDetail, Account account, Merchant merchant){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderMqInfo.getOrgOrderNo().toString());
        orderInfo.setAccountCode(accountDeductionDetail.getAccountCode());
        orderInfo.setOrderTime(orderMqInfo.getOrderTime());
        orderInfo.setOrderAmount(orderMqInfo.getOrderAmount());
        orderInfo.setOrderWholesaleAmount(orderMqInfo.getOrderWholesaleAmount());
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setCardId(accountDeductionDetail.getCardId());
        orderInfo.setStoreCode(orderMqInfo.getStoreCode());
        orderInfo.setTransNo(orderMqInfo.getTradeNo());
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setMerchantCode(account.getMerCode());
        orderInfo.setMerchantName(merchant.getMerName());
        orderInfo.setTransType(WelfareConstant.TransType.CONSUME.code());
        orderInfo.setTransTypeName(WelfareConstant.TransType.CONSUME.desc());
        return orderInfo;
    }

    public List<OrderInfoDetail> parseOrderInfoDetails(WelfareConstant.TransType transType){
        if (CollectionUtils.isEmpty(orderDetails)) {
            return Collections.emptyList();
        }
        return orderDetails.stream().map(orderDetailMqInfo -> {
            OrderInfoDetail detail = new OrderInfoDetail();
            detail.setOrderId(orgOrderNo.toString());
            detail.setCount(orderDetailMqInfo.getCount());
            detail.setProductId(orderDetailMqInfo.getProductId());
            detail.setRefundCount(orderDetailMqInfo.getRefundCount());
            detail.setUuid(orderDetailMqInfo.getUuid());
            detail.setSkuId(orderDetailMqInfo.getSkuId());
            detail.setSkuNo(orderDetailMqInfo.getSkuNo());
            detail.setSkuName(orderDetailMqInfo.getSkuName());
            detail.setWholesaleAmount(orderDetailMqInfo.getWholesaleAmount());
            BigDecimal taxRate = orderDetailMqInfo.getWholesaleTaxRate();
            if(Objects.nonNull(taxRate)){
                taxRate = taxRate.divide(new BigDecimal("100"), 4,RoundingMode.HALF_UP);
                detail.setWholesaleTaxRate(taxRate);
            }
            detail.setWholesalePrice(orderDetailMqInfo.getWholesalePrice());
            detail.setOriginalAmount(orderDetailMqInfo.getOriginalAmount());
            detail.setTransAmount(computeTransAmount(orderDetailMqInfo));
            detail.setTransNo(this.tradeNo);
            detail.setTransType(transType.code());
            return detail;
        }).collect(Collectors.toList());
    }

    public BigDecimal computeTransAmount(OrderDetailMqInfo orderDetailMqInfo){
        return orderDetailMqInfo.getSaleAmount();
    }

}
