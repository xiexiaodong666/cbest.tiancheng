package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * 用户交易流水明细表(account_deduction_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 14:23:38
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
    private String accountCode;
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
    private BigDecimal transType;
    /**
     * pos标识
     */
    @ApiModelProperty("pos标识")   
    private String pos;
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
     * 支付编码
     */
    @ApiModelProperty("支付编码")   
    private String payCode;
    /**
     * 子账户类型(例如餐费、交通费等)
     */
    @ApiModelProperty("子账户类型(例如餐费、交通费等)")   
    private BigDecimal merAccountType;
    /**
     * 子账户扣款金额
     */
    @ApiModelProperty("子账户扣款金额")   
    private BigDecimal accountDeductionAmount;
    /**
     * 子账户剩余金额
     */
    @ApiModelProperty("子账户剩余金额")   
    private BigDecimal accountDeductionBalance;
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
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic @TableField  
    private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
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
    public static final String ACCOUNT_DEDUCTION_BALANCE = "account_deduction_balance";
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

}