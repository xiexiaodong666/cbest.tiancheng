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
 * (merchant_bill_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_bill_detail")
@ApiModel("")
public class MerchantBillDetail extends Model<MerchantBillDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Integer id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")   
    private String transNo;
    /**
     * 交易类型(消费、退款、添加余额、添加额度等)
     */
    @ApiModelProperty("交易类型(消费、退款、添加余额、添加额度等)")   
    private String transType;
    /**
     * 余额类型(余额、可用信用额度、最大额度、充值额度)
     */
    @ApiModelProperty("余额类型(余额、可用信用额度、最大额度、充值额度)")   
    private String balanceType;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")   
    private BigDecimal transAmount;
    /**
     * 充值额度
     */
    @ApiModelProperty("充值额度")   
    private BigDecimal rechargeLimit;
    /**
     * 当前余额
     */
    @ApiModelProperty("当前余额")   
    private BigDecimal currentBalance;
    /**
     * 最高信用额度
     */
    @ApiModelProperty("最高信用额度")   
    private BigDecimal creditLimit;
    /**
     * 剩余信用额度
     */
    @ApiModelProperty("剩余信用额度")   
    private BigDecimal remainingLimit;
    /**
     * 返利额度
     */
    @ApiModelProperty("返利额度")   
    private BigDecimal rebateLimit;
    /**
     * 自主充值余额
     */
    @ApiModelProperty("自主充值余额")
    private BigDecimal selfDepositBalance;
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
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 交易流水号
    */
    public static final String TRANS_NO = "trans_no";
    /**
    * 交易类型(消费、退款、添加余额、添加额度等)
    */
    public static final String TRANS_TYPE = "trans_type";
    /**
    * 余额类型(余额、可用信用额度、最大额度、充值额度)
    */
    public static final String BALANCE_TYPE = "balance_type";
    /**
    * 交易金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 充值额度
    */
    public static final String RECHARGE_LIMIT = "recharge_limit";
    /**
    * 当前余额
    */
    public static final String CURRENT_BALANCE = "current_balance";
    /**
    * 最高信用额度
    */
    public static final String CREDIT_LIMIT = "credit_limit";
    /**
    * 剩余信用额度
    */
    public static final String REMAINING_LIMIT = "remaining_limit";
    /**
    * 返利额度
    */
    public static final String REBATE_LIMIT = "rebate_limit";
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