package com.welfare.servicesettlement.dto.mall;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@ApiModel
@Data
public class PlatformAftersaleDetailInfo {

    @ApiModelProperty(value = "订单主键ID")
    private Long id;

    @ApiModelProperty(value = "下单日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderDate;

    @ApiModelProperty(value = "下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "订单号")
    private Long orgOrderNo;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "支付方式 1：微信 2：支付宝 3：云闪付")
    private Integer payType;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @ApiModelProperty(value = "是否配送订单 0：是 1：否")
    private Integer isDelivery;

    @ApiModelProperty(value = "订单状态码")
    private Integer statusCode;

    @ApiModelProperty(value = "订单状态名称")
    private String statusName;

    @ApiModelProperty(value = "用户id")
    private Long memberId;

    @ApiModelProperty(value = "用户名")
    private String memberName;

    @ApiModelProperty(value = "用户联系方式")
    private String memberPhone;

    @ApiModelProperty(value = "用户名")
    private String receiveName;

    @ApiModelProperty(value = "收货地址")
    private String receiveAddr;

    @ApiModelProperty(value = "收货地址经度")
    private String receiveAddrLongitude;

    @ApiModelProperty(value = "收货地址纬度")
    private String receiveAddrLatitude;

    @ApiModelProperty(value = "收货联系方式")
    private String receivePhone;

    @ApiModelProperty(value = "订单总额 = 销售总额+包装费+运费")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "实际商品售价总额 = 商品售价总额-折扣总额")
    private BigDecimal saleAmount;

    @ApiModelProperty(value = "商品原价总额")
    private BigDecimal originalAmount;

    @ApiModelProperty(value = "商家促销总额")
    private BigDecimal supplierDiscountAmount;

    @ApiModelProperty(value = "平台促销总额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "采购价总额")
    private BigDecimal purchaseAmount;

    @ApiModelProperty(value = "包装费")
    private BigDecimal packageAmount;

    @ApiModelProperty(value = "运费")
    private BigDecimal fare;

    @ApiModelProperty(value = "拣货退差总额")
    private BigDecimal orderRefundAmount;

    @ApiModelProperty(value = "订单总重量")
    private Integer orderWeight;

    @ApiModelProperty(value = "订单总商品数量")
    private Integer orderCount;

    @ApiModelProperty(value = "预计送达/自提开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectReceiveStartTime;

    @ApiModelProperty(value = "预计送达/自提结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectReceiveEndTime;

    @ApiModelProperty(value = "实际送达/自提/核销时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    @ApiModelProperty(value = "投递次数")
    private Integer deliveredTimes;

    @ApiModelProperty(value = "订单备注")
    private String remark;

    @ApiModelProperty(value = "取餐码")
    private String mealCode;

    @ApiModelProperty(value = "自提码")
    private String deliveryCode;

    @ApiModelProperty(value = "系统来源 0：o2o 1：扫码购 2：自助购")
    private Integer systemSrc;

    @ApiModelProperty(value = "是否申请售后(0:否 1:是)")
    private Integer isAftersale;

    @ApiModelProperty(value = "是否存在售后退款(0:否 1:部分退款完成 2:全部退款完成)")
    private Integer isAftersaleRefund;

    @ApiModelProperty(value = "是否已开票 0：否 1：是")
    private Integer isBill;

    @ApiModelProperty(value = "是否已兑换 0：否 1：是")
    private Integer isExchange;

    @ApiModelProperty(value = "是否已退运费 0：否 1：是")
    private Integer isReturnFreight;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "商户ID")
    private Long supplierId;

    @ApiModelProperty(value = "商户名称")
    private String supplierName;

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
}
