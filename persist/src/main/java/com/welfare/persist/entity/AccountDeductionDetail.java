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
 * 用户交易流水明细表(account_deduction_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:22
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_deduction_detail")
@ApiModel("用户交易流水明细表")
public class AccountDeductionDetail extends Model<AccountDeductionDetail> implements Serializable {
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
     * 关联交易单号（退款时使用）
     */
    @ApiModelProperty("关联交易单号（退款时使用）")   
    private String relatedTransNo;
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
     * 渠道(自主充值需要显示来源:支付宝、微信)
     */
    @ApiModelProperty("渠道(自主充值需要显示来源:支付宝、微信)")   
    private String chanel;
    /**
     * 交易总金额
     */
    @ApiModelProperty("交易总金额")   
    private BigDecimal transAmount;
    @ApiModelProperty("此笔交易已逆向金额,用于标记付款流水的已退款金额")
    private BigDecimal reversedAmount;
    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")   
    private Date transTime;
    /**
     * 支付编码
     */
    @ApiModelProperty("支付编码")   
    private String payCode;
    /**
     * 子账户类型(例如餐费、交通费等)
     */
    @ApiModelProperty("子账户类型(例如餐费、交通费等)")   
    private String merAccountType;
    /**
     * 子账户扣款金额
     */
    @ApiModelProperty("子账户扣款金额")   
    private BigDecimal accountDeductionAmount;
    /**
     * 子账户剩余金额
     */
    @ApiModelProperty("子账户剩余金额")   
    private BigDecimal accountAmountTypeBalance;
    /**
     * 商户余额扣款金额
     */
    @ApiModelProperty("商户余额扣款金额")   
    private BigDecimal merDeductionAmount;
    /**
     * 商户额度扣款金额
     */ 
    @ApiModelProperty("商户额度扣款金额")   
    private BigDecimal merDeductionCreditAmount;
    /**
     * 自费扣款金额
     */
    @ApiModelProperty("自费扣款金额")   
    private BigDecimal selfDeductionAmount;

    @ApiModelProperty("订单渠道")
    private String orderChannel;

    @ApiModelProperty("支付渠道")
    private String paymentChannel;

    @ApiModelProperty("福利分组id")
    private Long accountAmountTypeGroupId;
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
    * 关联交易单号（退款时使用）
    */
    public static final String RELATED_TRANS_NO = "related_trans_no";
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
    * 渠道(自主充值需要显示来源:支付宝、微信)
    */
    public static final String CHANEL = "chanel";
    /**
    * 交易总金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 交易时间
    */
    public static final String TRANS_TIME = "trans_time";
    /**
    * 支付编码
    */
    public static final String PAY_CODE = "pay_code";
    /**
    * 子账户类型(例如餐费、交通费等)
    */
    public static final String MER_ACCOUNT_TYPE = "mer_account_type";
    /**
    * 子账户扣款金额
    */
    public static final String ACCOUNT_DEDUCTION_AMOUNT = "account_deduction_amount";
    /**
    * 子账户剩余金额
    */
    public static final String ACCOUNT_AMOUNT_TYPE_BALANCE = "account_amount_type_balance";
    /**
    * 商户余额扣款金额
    */
    public static final String MER_DEDUCTION_AMOUNT = "mer_deduction_amount";
    /**
    * 商户额度扣款金额
    */
    public static final String MER_DEDUCTION_CREDIT_AMOUNT = "mer_deduction_credit_amount";
    /**
    * 自费扣款金额
    */
    public static final String SELF_DEDUCTION_AMOUNT = "self_deduction_amount";
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

    public static final String PAYMENT_CHANNEL = "payment_channel";

    public static final String ACCOUNT_AMOUNT_TYPE_GROUP_ID = "account_amount_type_group_id";

}