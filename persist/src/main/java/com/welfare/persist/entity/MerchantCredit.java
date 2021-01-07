package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
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
 * 商户额度信(merchant_credit)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 16:35:13
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_credit")
@ApiModel("商户额度信")
public class MerchantCredit extends Model<MerchantCredit> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")  
    private String merCode;
    /**
     * 充值额度
     */
    @ApiModelProperty("充值额度")  
    private BigDecimal rechargeLimit;
    /**
     * 目前余额
     */
    @ApiModelProperty("目前余额")  
    private BigDecimal currentBalance;
    /**
     * 信用额度
     */
    @ApiModelProperty("信用额度")  
    private BigDecimal creditLimit;
    /**
     * 剩余信用额度
     */
    @ApiModelProperty("剩余信用额度")  
    private BigDecimal remainingLimit;
    /**
     * 返利余额
     */
    @ApiModelProperty("返利余额")  
    private BigDecimal rebateLimit;
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
     * 删除标志
     */
    @ApiModelProperty("删除标志")  
    private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本") @Version 
    private Integer version;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 充值额度
    */
    public static final String RECHARGE_LIMIT = "recharge_limit";
    /**
    * 目前余额
    */
    public static final String CURRENT_BALANCE = "current_balance";
    /**
    * 信用额度
    */
    public static final String CREDIT_LIMIT = "credit_limit";
    /**
    * 剩余信用额度
    */
    public static final String REMAINING_LIMIT = "remaining_limit";
    /**
    * 返利余额
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
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}