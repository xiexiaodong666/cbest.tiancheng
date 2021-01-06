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
 * 充值申请明细(account_deposit_apply_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_deposit_apply_detail")
@ApiModel("充值申请明细")
public class AccountDepositApplyDetail extends Model<AccountDepositApplyDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 申请编码
     */
    @ApiModelProperty("申请编码")  
    private String applyCode;
    /**
     * 员工账户
     */
    @ApiModelProperty("员工账户")  
    private String accountCode;
    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")  
    private BigDecimal rechargeAmount;
    /**
     * 充值状态
     */
    @ApiModelProperty("充值状态")  
    private Integer rechargeStatus;
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
     * 更新日期
     */
    @ApiModelProperty("更新日期")  
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