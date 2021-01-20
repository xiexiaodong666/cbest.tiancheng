package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (order_info)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_info")
@ApiModel("")
public class OrderInfo extends Model<OrderInfo> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Integer id;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")   
    private String orderId;
    /**
     * 商品
     */
    @ApiModelProperty("商品")   
    private String goods;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merchantCode;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")   
    private String merchantName;
    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")   
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")   
    private String storeName;
    /**
     * 账户
     */
    @ApiModelProperty("账户")   
    private Long accountCode;
    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")   
    private String accountName;
    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")   
    private String transType;
    /**
     * 交易类型名称
     */
    @ApiModelProperty("交易类型名称")   
    private String transTypeName;
    /**
     * 卡号
     */
    @ApiModelProperty("卡号")   
    private Integer cardId;
    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")   
    private BigDecimal orderAmount;
    /**
     * 订单时间
     */
    @ApiModelProperty("订单时间")   
    private Date orderTime;
    /**
     * 支付编码
     */
    @ApiModelProperty("支付编码")   
    private String payCode;
    /**
     * 支付名称
     */
    @ApiModelProperty("支付名称")   
    private String payName;
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

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 订单号
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 商品
    */
    public static final String GOODS = "goods";
    /**
    * 商户代码
    */
    public static final String MERCHANT_CODE = "merchant_code";
    /**
    * 商户名称
    */
    public static final String MERCHANT_NAME = "merchant_name";
    /**
    * 门店编码
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 门店名称
    */
    public static final String STORE_NAME = "store_name";
    /**
    * 账户
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 账户名称
    */
    public static final String ACCOUNT_NAME = "account_name";
    /**
    * 交易类型
    */
    public static final String TRANS_TYPE = "trans_type";
    /**
    * 交易类型名称
    */
    public static final String TRANS_TYPE_NAME = "trans_type_name";
    /**
    * 卡号
    */
    public static final String CARD_ID = "card_id";
    /**
    * 订单金额
    */
    public static final String ORDER_AMOUNT = "order_amount";
    /**
    * 订单时间
    */
    public static final String ORDER_TIME = "order_time";
    /**
    * 支付编码
    */
    public static final String PAY_CODE = "pay_code";
    /**
    * 支付名称
    */
    public static final String PAY_NAME = "pay_name";
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

}