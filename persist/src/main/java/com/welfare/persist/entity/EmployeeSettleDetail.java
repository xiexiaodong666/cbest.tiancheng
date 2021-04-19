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
 * (employee_settle_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("employee_settle_detail")
@ApiModel("")
public class EmployeeSettleDetail extends Model<EmployeeSettleDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 账单编号
     */
    @ApiModelProperty("账单编号")   
    private String settleNo;
    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")   
    private String orderId;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")   
    private String transNo;
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
     * 卡号
     */
    @ApiModelProperty("卡号")   
    private Integer cardId;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")   
    private String merName;
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
     * 交易时间
     */
    @ApiModelProperty("交易时间")   
    private Date transTime;
    /**
     * pos机器编码
     */
    @ApiModelProperty("pos机器编码")   
    private String pos;
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
     * 交易类型
     */
    @ApiModelProperty("交易类型")   
    private String transType;
    /**
     * 交易类型名
     */
    @ApiModelProperty("交易类型名")   
    private String transTypeName;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")   
    private BigDecimal transAmount;
    /**
     * 福利类型(个人授信，个人授信溢缴款)
     */
    @ApiModelProperty("福利类型(个人授信，个人授信溢缴款)")   
    private String merAccountType;
    /**
     * 福利类型(个人授信，个人授信溢缴款)
     */
    @ApiModelProperty("福利类型(个人授信，个人授信溢缴款)")   
    private String merAccountTypeName;
    /**
     * 子账户扣款金额
     */
    @ApiModelProperty("子账户扣款金额")   
    private BigDecimal accountAmount;
    /**
     * 子账户余额
     */
    @ApiModelProperty("子账户余额")   
    private BigDecimal accountBalance;
    /**
     * 商户余额扣款金额
     */
    @ApiModelProperty("商户余额扣款金额")   
    private BigDecimal merDeductionAmount;
    /**
     * 商户信用扣款金额
     */
    @ApiModelProperty("商户信用扣款金额")   
    private BigDecimal merCreditDeductionAmount;
    /**
     * 结算标志 settled已结算 settling结算中 unsettled未结算
     */
    @ApiModelProperty("结算标志 settled已结算 settling结算中 unsettled未结算")   
    private String settleFlag;
    /**
     * 订单渠道
     */
    @ApiModelProperty("订单渠道")   
    private String orderChannel;
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
    /**
     * 商户授信额度
     */
    @ApiModelProperty("商户授信额度")   
    private BigDecimal merCredit;
    /**
     * 商户余额
     */
    @ApiModelProperty("商户余额")   
    private BigDecimal merBalance;
    /**
     * 门店类型
     */
    @ApiModelProperty("门店类型(自营:self,第三方:third)")
    private String storeType;
    /**
     * 用户交易流水明细表主键
     */
    @ApiModelProperty("用户交易流水明细表主键")
    private Long accountDeductionAmountId;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 账单编号
    */
    public static final String SETTLE_NO = "settle_no";
    /**
    * 订单编码
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 交易流水号
    */
    public static final String TRANS_NO = "trans_no";
    /**
    * 账户
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 账户名称
    */
    public static final String ACCOUNT_NAME = "account_name";
    /**
    * 卡号
    */
    public static final String CARD_ID = "card_id";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 商户名称
    */
    public static final String MER_NAME = "mer_name";
    /**
    * 门店编码
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 门店名称
    */
    public static final String STORE_NAME = "store_name";
    /**
    * 交易时间
    */
    public static final String TRANS_TIME = "trans_time";
    /**
    * pos机器编码
    */
    public static final String POS = "pos";
    /**
    * 支付编码
    */
    public static final String PAY_CODE = "pay_code";
    /**
    * 支付名称
    */
    public static final String PAY_NAME = "pay_name";
    /**
    * 交易类型
    */
    public static final String TRANS_TYPE = "trans_type";
    /**
    * 交易类型名
    */
    public static final String TRANS_TYPE_NAME = "trans_type_name";
    /**
    * 交易金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 福利类型(个人授信，个人授信溢缴款)
    */
    public static final String MER_ACCOUNT_TYPE = "mer_account_type";
    /**
    * 福利类型(个人授信，个人授信溢缴款)
    */
    public static final String MER_ACCOUNT_TYPE_NAME = "mer_account_type_name";
    /**
    * 子账户扣款金额
    */
    public static final String ACCOUNT_AMOUNT = "account_amount";
    /**
    * 子账户余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 商户余额扣款金额
    */
    public static final String MER_DEDUCTION_AMOUNT = "mer_deduction_amount";
    /**
    * 商户信用扣款金额
    */
    public static final String MER_CREDIT_DEDUCTION_AMOUNT = "mer_credit_deduction_amount";
    /**
    * 结算标志 settled已结算 settling结算中 unsettled未结算
    */
    public static final String SETTLE_FLAG = "settle_flag";
    /**
    * 订单渠道
    */
    public static final String ORDER_CHANNEL = "order_channel";
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
    /**
    * 商户授信额度
    */
    public static final String MER_CREDIT = "mer_credit";
    /**
    * 商户余额
    */
    public static final String MER_BALANCE = "mer_balance";
    /**
     * 门店类型
     */
    public static final String STORE_TYPE = "storeType";
    /**
     * 用户交易流水明细表主键
     */
    public static final String ACCOUNT_DEDUCTION_AMOUNT_ID = "account_deduction_amount_id";
}