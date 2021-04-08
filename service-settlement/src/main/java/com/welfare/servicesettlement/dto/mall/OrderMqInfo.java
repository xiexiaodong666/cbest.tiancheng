package com.welfare.servicesettlement.dto.mall;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.OrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public OrderInfo parseToOrderInfo(OrderMqInfo orderMqInfo, AccountDeductionDetail accountDeductionDetail, Account account, Merchant merchant){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderMqInfo.getOrgOrderNo().toString());
        orderInfo.setAccountCode(accountDeductionDetail.getAccountCode());
        orderInfo.setOrderTime(orderMqInfo.getOrderTime());
        orderInfo.setOrderAmount(orderMqInfo.getOrderAmount());
        orderInfo.setAccountName("todo");
        orderInfo.setCardId(accountDeductionDetail.getCardId());
        //orderInfo.setStoreCode(this.storeId);
        orderInfo.setTransNo(this.tradeNo);
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setMerchantCode(account.getMerCode());
        orderInfo.setMerchantName(merchant.getMerName());
        orderInfo.setTransType(WelfareConstant.TransType.CONSUME.code());
        orderInfo.setTransTypeName(WelfareConstant.TransType.CONSUME.name());
        return orderInfo;
    }
}
