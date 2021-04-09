package com.welfare.servicesettlement.dto.mall;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
@Data
public class OrderDetailInfo implements Serializable {
    private static final long serialVersionUID = -824084819735145493L;
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
}
