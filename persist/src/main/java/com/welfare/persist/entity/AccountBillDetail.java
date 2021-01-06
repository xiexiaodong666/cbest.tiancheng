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
 * 用户流水明细(account_bill_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_bill_detail")
@ApiModel("用户流水明细")
public class AccountBillDetail extends Model<AccountBillDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
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
     * 账单流水号
     */
    @ApiModelProperty("账单流水号")  
    private String billNo;
    /**
     * 消费时间
     */
    @ApiModelProperty("消费时间")  
    private Date orderTime;
    /**
     * 账单类型
     */
    @ApiModelProperty("账单类型")  
    private String billType;
    /**
     * 消费门店
     */
    @ApiModelProperty("消费门店")  
    private String storeCode;
    /**
     * 金额
     */
    @ApiModelProperty("金额")  
    private BigDecimal realAmount;
    /**
     * 账户余额
     */
    @ApiModelProperty("账户余额")  
    private BigDecimal accountBalance;
    /**
     * pos标识
     */
    @ApiModelProperty("pos标识")  
    private String pos;
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
     * 支付类型
     */
    @ApiModelProperty("支付类型")  
    private String paymentType;
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
    private Boolean flag;
    /**
     * 版本
     */
    @ApiModelProperty("版本") @Version 
    private Integer version;

}