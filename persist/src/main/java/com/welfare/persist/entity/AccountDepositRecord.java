package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 账号充值记录表(account_deposit_record)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_deposit_record")
@ApiModel("账号充值记录表")
public class AccountDepositRecord extends Model<AccountDepositRecord> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 员工账号
     */
    @ApiModelProperty("员工账号")   
    private Integer accountCode;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")   
    private String payType;
    /**
     * 支付交易号
     */
    @ApiModelProperty("支付交易号")   
    private String payTradeNo;
    /**
     * 支付重百付交易号
     */
    @ApiModelProperty("支付重百付交易号")   
    private String payGatewayTradeNo;
    /**
     * 支付渠道交易号
     */
    @ApiModelProperty("支付渠道交易号")   
    private String payChannelTradeNo;
    /**
     * 充值交易号
     */
    @ApiModelProperty("充值交易号")   
    private String depositTradeNo;
    /**
     * 充值重百付交易号
     */
    @ApiModelProperty("充值重百付交易号")   
    private String depositGatewayTradeNo;
    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")   
    private BigDecimal depositAmount;
    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")   
    private Date payTime;
    /**
     * 充值时间
     */
    @ApiModelProperty("充值时间")   
    private Date depositTime;
    /**
     * 支付状态
     */
    @ApiModelProperty("支付状态")   
    private Integer payStatus;
    /**
     * 充值状态
     */
    @ApiModelProperty("充值状态")   
    private Integer rechargeStatus;
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
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 员工账号
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 支付方式
    */
    public static final String PAY_TYPE = "pay_type";
    /**
    * 支付交易号
    */
    public static final String PAY_TRADE_NO = "pay_trade_no";
    /**
    * 支付重百付交易号
    */
    public static final String PAY_GATEWAY_TRADE_NO = "pay_gateway_trade_no";
    /**
    * 支付渠道交易号
    */
    public static final String PAY_CHANNEL_TRADE_NO = "pay_channel_trade_no";
    /**
    * 充值交易号
    */
    public static final String DEPOSIT_TRADE_NO = "deposit_trade_no";
    /**
    * 充值重百付交易号
    */
    public static final String DEPOSIT_GATEWAY_TRADE_NO = "deposit_gateway_trade_no";
    /**
    * 充值金额
    */
    public static final String DEPOSIT_AMOUNT = "deposit_amount";
    /**
    * 支付时间
    */
    public static final String PAY_TIME = "pay_time";
    /**
    * 充值时间
    */
    public static final String DEPOSIT_TIME = "deposit_time";
    /**
    * 支付状态
    */
    public static final String PAY_STATUS = "pay_status";
    /**
    * 充值状态
    */
    public static final String RECHARGE_STATUS = "recharge_status";
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
    * 版本
    */
    public static final String VERSION = "version";

}