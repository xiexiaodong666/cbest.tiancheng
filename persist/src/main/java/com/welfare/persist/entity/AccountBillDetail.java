package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 用户交易流水明细表(account_bill_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-08 11:23:04
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
     * 交易总金额
     */
    @ApiModelProperty("交易总金额")   
    private BigDecimal transAllAmount;
    /**
     * 交易商户信用额度
     */
    @ApiModelProperty("交易商户信用额度")   
    private BigDecimal transAmountCredit;
    /**
     * 交易自费额度
     */
    @ApiModelProperty("交易自费额度")   
    private BigDecimal transAmountSelf;
    /**
     * 交易商户余额
     */
    @ApiModelProperty("交易商户余额")   
    private BigDecimal transAmountBalance;
    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")   
    private Date transTime;
    /**
     * 消费门店
     */
    @ApiModelProperty("消费门店")   
    private String storeCode;
    /**
     * 账户余额
     */
    @ApiModelProperty("账户余额")   
    private BigDecimal accountBalance;
    /**
     * 账户自费余额
     */
    @ApiModelProperty("账户自费余额")   
    private BigDecimal accountSelfBalance;
    /**
     * 账户福利余额
     */
    @ApiModelProperty("账户福利余额")   
    private BigDecimal accountWelfareBalance;
    /**
     * pos标识
     */
    @ApiModelProperty("pos标识")   
    private String pos;
    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")   
    private BigDecimal transType;
    /**
     * 支付ID
     */
    @ApiModelProperty("支付ID")   
    private String paymentId;
    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")   
    private String orderId;
    /**
     * 支付类型代码
     */
    @ApiModelProperty("支付类型代码")   
    private String paymentTypeCode;
    /**
     * 支付类型名称
     */
    @ApiModelProperty("支付类型名称")   
    private String paymentTypeName;
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
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic  
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
    * 交易总金额
    */
    public static final String TRANS_ALL_AMOUNT = "trans_all_amount";
    /**
    * 交易商户信用额度
    */
    public static final String TRANS_AMOUNT_CREDIT = "trans_amount_credit";
    /**
    * 交易自费额度
    */
    public static final String TRANS_AMOUNT_SELF = "trans_amount_self";
    /**
    * 交易商户余额
    */
    public static final String TRANS_AMOUNT_BALANCE = "trans_amount_balance";
    /**
    * 交易时间
    */
    public static final String TRANS_TIME = "trans_time";
    /**
    * 消费门店
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 账户余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 账户自费余额
    */
    public static final String ACCOUNT_SELF_BALANCE = "account_self_balance";
    /**
    * 账户福利余额
    */
    public static final String ACCOUNT_WELFARE_BALANCE = "account_welfare_balance";
    /**
    * pos标识
    */
    public static final String POS = "pos";
    /**
    * 交易类型
    */
    public static final String TRANS_TYPE = "trans_type";
    /**
    * 支付ID
    */
    public static final String PAYMENT_ID = "payment_id";
    /**
    * 订单ID
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 支付类型代码
    */
    public static final String PAYMENT_TYPE_CODE = "payment_type_code";
    /**
    * 支付类型名称
    */
    public static final String PAYMENT_TYPE_NAME = "payment_type_name";
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