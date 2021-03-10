package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 用户交易流水明细表(account_bill_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:22
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_bill_detail")
@ApiModel("用户交易流水明细表")
public class AccountBillDetail extends Model<AccountBillDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
	private Long id;
    /**
     * 员工账号
     */
    @ApiModelProperty("员工账号")   
    private Long accountCode;
    /**
     * 卡号
     */
    @ApiModelProperty("卡号")   
    private String cardId;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")   
    private String transNo;
    /**
     * 消费门店
     */
    @ApiModelProperty("消费门店")   
    private String storeCode;
    /**
     * 交易类型(消费、退款、充值等)
     */
    @ApiModelProperty("交易类型(消费、退款、充值等)")   
    private String transType;
    /**
     * pos标识
     */
    @ApiModelProperty("pos标识")   
    private String pos;
    /**
     * 充值渠道(第三方充值需要体现:支付宝或者微信)
     */
    @ApiModelProperty("充值渠道(第三方充值需要体现:支付宝或者微信)")   
    private String channel;
    /**
     * 交易总金额
     */
    @ApiModelProperty("交易总金额")   
    private BigDecimal transAmount;
    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")   
    private Date transTime;
    /**
     * 账户余额
     */
    @ApiModelProperty("账户余额")   
    private BigDecimal accountBalance;
    /**
     * 授信余额
     */
    @ApiModelProperty("授信余额")   
    private BigDecimal surplusQuota;
    @ApiModelProperty("个人授信溢缴款")
    private BigDecimal surplusQuotaOverpay;
    @ApiModelProperty("订单渠道（o2o,online,shop_shopping）")
    private String orderChannel;
    @ApiModelProperty("支付方式,二维码、卡、线上")
    private String paymentType;
    @ApiModelProperty("支付方式信息，二维码条码或者卡磁条号")
    private String paymentTypeInfo;
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
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic   
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
    * id
    */
    public static final String ID = "id";
    /**
    * 员工账号
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 卡号
    */
    public static final String CARD_ID = "card_id";
    /**
    * 交易流水号
    */
    public static final String TRANS_NO = "trans_no";
    /**
    * 消费门店
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 交易类型(消费、退款、充值等)
    */
    public static final String TRANS_TYPE = "trans_type";
    /**
    * pos标识
    */
    public static final String POS = "pos";
    /**
    * 充值渠道(第三方充值需要体现:支付宝或者微信)
    */
    public static final String CHANNEL = "channel";
    /**
    * 交易总金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 交易时间
    */
    public static final String TRANS_TIME = "trans_time";
    /**
    * 账户余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 授信余额
    */
    public static final String SURPLUS_QUOTA = "surplus_quota";
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
    * 删除标志  1-删除、0-未删除
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    public static final String ORDER_CHANNEL = "order_channel";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_TYPE_INFO = "payment_type_info";
    public static final String SURPLUS_QUOTA_OVERPAY = "surplus_quota_overpay";

}