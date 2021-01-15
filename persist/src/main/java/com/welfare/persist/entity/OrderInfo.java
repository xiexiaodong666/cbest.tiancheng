package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (order_info)实体类
 *
 * @author kancy
 * @since 2021-01-12 17:25:14
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_info")
@ApiModel("订单实体")
public class OrderInfo extends Model<OrderInfo> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
	private Integer id;
    /**
     * orderId
     */
    @ApiModelProperty("orderId")  
    private String orderId;
    /**
     * goods
     */
    @ApiModelProperty("goods")  
    private String goods;
    /**
     * merchantCode
     */
    @ApiModelProperty("merchantCode")  
    private String merchantCode;
    /**
     * merchantName
     */
    @ApiModelProperty("merchantName")  
    private String merchantName;
    /**
     * storeCode
     */
    @ApiModelProperty("storeCode")  
    private String storeCode;
    /**
     * storeName
     */
    @ApiModelProperty("storeName")  
    private String storeName;
    /**
     * accountCode
     */
    @ApiModelProperty("accountCode")  
    private Integer accountCode;
    /**
     * accountName
     */
    @ApiModelProperty("accountName")  
    private String accountName;
    /**
     * cardId
     */
    @ApiModelProperty("cardId")  
    private Integer cardId;
    /**
     * orderAmount
     */
    @ApiModelProperty("orderAmount")  
    private BigDecimal orderAmount;
    /**
     * orderTime
     */
    @ApiModelProperty("orderTime")  
    private String orderTime;
    /**
     * payCode
     */
    @ApiModelProperty("payCode")  
    private String payCode;
    /**
     * payName
     */
    @ApiModelProperty("payName")  
    private String payName;
    /**
     * transType
     */
    @ApiModelProperty("transType")
    private String transType;
    /**
     * transTypeName
     */
    @ApiModelProperty("transTypeName")
    private String transTypeName;
    /**
     * createUser
     */
    @ApiModelProperty("createUser")  
    private String createUser;
    /**
     * createTime
     */
    @ApiModelProperty("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 
    */
    public static final String GOODS = "goods";
    /**
    * 
    */
    public static final String MERCHANT_CODE = "merchant_code";
    /**
    * 
    */
    public static final String MERCHANT_NAME = "merchant_name";
    /**
    * 
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 
    */
    public static final String STORE_NAME = "store_name";
    /**
    * 
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 
    */
    public static final String ACCOUNT_NAME = "account_name";
    /**
    * 
    */
    public static final String CARD_ID = "card_id";
    /**
    * 
    */
    public static final String ORDER_AMOUNT = "order_amount";
    /**
    * 
    */
    public static final String ORDER_TIME = "order_time";
    /**
    * 
    */
    public static final String PAY_CODE = "pay_code";
    /**
    * 
    */
    public static final String PAY_NAME = "pay_name";
    /**
     *
     */
    public static final String TRANS_TYPE = "trans_type";
    /**
     *
     */
    public static final String TRANS_TYPE_NAME = "trans_type_name";
    /**
    * 
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 
    */
    public static final String CREATE_TIME = "create_time";

}