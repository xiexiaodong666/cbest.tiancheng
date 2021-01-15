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
 * 充值申请明细(account_deposit_apply_detail)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
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
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
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
    private Long accountCode;
    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")   
    private BigDecimal rechargeAmount;
    /**
     * 充值状态
     */
    @ApiModelProperty("充值状态")   
    private String rechargeStatus;
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
     * 更新日期
     */
    @ApiModelProperty("更新日期")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Integer version;
    /**
     * 流水号
     */
    @ApiModelProperty("流水号")
    private String transNo;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 申请编码
    */
    public static final String APPLY_CODE = "apply_code";
    /**
    * 员工账户
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 充值金额
    */
    public static final String RECHARGE_AMOUNT = "recharge_amount";
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
    * 更新日期
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
    /**
     * 流水号
     */
    public static final String TRANS_NO = "transNo";
}