package com.welfare.servicesettlement.dto.mall;

import com.welfare.persist.entity.OrderInfoDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/27/2021
 */
@Data
public class OrderDetailMqInfo implements Serializable {
    /**
     * 品牌id
     */
    private String brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 一级品类id
     */
    private String categoryFirstId;
    /**
     * 一级品类名称
     */
    private String categoryFirstName;
    /**
     * 品类id
     */
    private String categoryId;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 商品数量
     */
    private Integer count;
    /**
     * 主键id
     */
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 商品原价总金额
     */
    private BigDecimal originalAmount;
    /**
     * 商品原价
     */
    private BigDecimal originalPrice;
    /**
     * 支付优惠
     */
    private BigDecimal payDiscountAmount;
    /**
     * plu码
     */
    private String plu;
    /**
     * 享受优惠的数量
     */
    private Integer proQty;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品图片地址
     */
    private String productPicPath;
    /**
     * 商品总量
     */
    private BigDecimal productWeight;
    /**
     * 订单项采购总额
     */
    private BigDecimal purchaseAmount;
    /**
     * 售后金额
     */
    private BigDecimal refundAmount;
    /**
     * 售后数量
     */
    private Integer refundCount;
    /**
     * 商品目前总金额，减去售后的
     */
    private BigDecimal saleAmount;
    /**
     * 商品销售单价
     */
    private BigDecimal salePrice;
    /**
     * 促销活动优惠金额
     */
    private BigDecimal salesDiscountAmount;
    /**
     * 商品skuId
     */
    private String skuId;
    /**
     * 商品sku名称
     */
    private String skuName;
    /**
     * 商品sku号
     */
    private String skuNo;
    /**
     * 规格
     */
    private String spec;
    /**
     * 商家订单项折扣总额
     */
    private BigDecimal supplierDiscountAmount;

    /**
     * 计价方式 1：计数， 2：散买
     */
    private String type;
    /**
     * 单位
     */
    private String unit;
    /**
     * 数据行uuid
     */
    private String uuid;
    /**
     * 国际条码
     */
    private String wareCode;
    /**
     * 商品总量
     */
    private BigDecimal weight;
    /**
     * 商品结算总金额
     */
    private BigDecimal wholesaleAmount;
    /**
     * 商品结算单价
     */
    private BigDecimal wholesalePrice;
    /**
     * 商品结算税率
     */
    private BigDecimal wholesaleTaxRate;
}
