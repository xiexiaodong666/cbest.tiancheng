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
 * (settle_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("settle_detail")
@ApiModel("")
public class SettleDetail extends Model<SettleDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Integer id;
    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")   
    private Integer orderId;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")   
    private Integer transNo;
    /**
     * 账户
     */
    @ApiModelProperty("账户")   
    private String accountCode;
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
    private String stroeName;
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
     * 福利类型(餐费、交通费等)
     */
    @ApiModelProperty("福利类型(餐费、交通费等)")   
    private String merAccountType;
    /**
     * 福利类型(餐费、交通费等)
     */
    @ApiModelProperty("福利类型(餐费、交通费等)")   
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
     * 自费扣款金额
     */
    @ApiModelProperty("自费扣款金额")   
    private BigDecimal selfDeductionAmount;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Integer version;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
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
    public static final String STROE_NAME = "stroe_name";
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
    * 福利类型(餐费、交通费等)
    */
    public static final String MER_ACCOUNT_TYPE = "mer_account_type";
    /**
    * 福利类型(餐费、交通费等)
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
    * 版本
    */
    public static final String VERSION = "version";

}