package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (order_info_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-04-27 15:27:24
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_info_detail")
@ApiModel("")
public class OrderInfoDetail extends Model<OrderInfoDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @ApiModelProperty("pk")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 订单id
     */
    @ApiModelProperty("订单id")   
    private String orderId;

    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")
    private String transNo;

    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")
    private String transType;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")   
    private String productId;
    /**
     * 行uuid
     */
    @ApiModelProperty("行uuid")   
    private String uuid;
    /**
     * 商品skuid
     */
    @ApiModelProperty("商品skuid")   
    private String skuId;
    /**
     * 商品skuNo
     */
    @ApiModelProperty("商品skuNo")   
    private String skuNo;
    /**
     * 商品sku名称
     */
    @ApiModelProperty("商品sku名称")   
    private String skuName;
    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")   
    private Integer count;
    /**
     * 售后数量
     */
    @ApiModelProperty("售后数量")   
    private Integer refundCount;

    @ApiModelProperty("交易金额")
    private BigDecimal transAmount;

    @ApiModelProperty("商品原价")
    private BigDecimal originalAmount;
    /**
     * 商品结算单价
     */
    @ApiModelProperty("商品结算单价")   
    private BigDecimal wholesalePrice;
    /**
     * 商品结算总金额
     */
    @ApiModelProperty("商品结算总金额")   
    private BigDecimal wholesaleAmount;
    /**
     * 税率
     */
    @ApiModelProperty("税率")   
    private BigDecimal wholesaleTaxRate;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT)
	private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    @TableField(fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;

//以下为列明常量

    /**
    * pk
    */
    public static final String ID = "id";
    /**
    * 订单id
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 商品id
    */
    public static final String PRODUCT_ID = "product_id";
    /**
    * 行uuid
    */
    public static final String UUID = "uuid";
    /**
    * 商品skuid
    */
    public static final String SKU_ID = "sku_id";
    /**
    * 商品skuNo
    */
    public static final String SKU_NO = "sku_no";
    /**
    * 商品sku名称
    */
    public static final String SKU_NAME = "sku_name";
    /**
    * 商品数量
    */
    public static final String COUNT = "count";
    /**
    * 售后数量
    */
    public static final String REFUND_COUNT = "refund_count";
    /**
    * 商品结算单价
    */
    public static final String WHOLESALE_PRICE = "wholesale_price";
    /**
    * 商品结算总金额
    */
    public static final String WHOLESALE_AMOUNT = "wholesale_amount";
    /**
    * 税率
    */
    public static final String WHOLESALE_TAX_RATE = "wholesale_tax_rate";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建时间
    */
    public static final String CREATE_TIME = "create_time";
    /**
    * 更新人
    */
    public static final String UPDATE_USER = "update_user";
    /**
    * 更新时间
    */
    public static final String UPDATE_TIME = "update_time";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    /**
     * 交易类型
     */
    public static final String TRANS_TYPE = "trans_type";
    /**
     * 交易流水号
     */
    public static final String TRANS_NO = "trans_no";
}