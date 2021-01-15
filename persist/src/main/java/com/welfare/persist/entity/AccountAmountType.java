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
 * (account_amount_type)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:22
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_amount_type")
@ApiModel("")
public class AccountAmountType extends Model<AccountAmountType> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ApiModelProperty("自增id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 账户编码
     */
    @ApiModelProperty("账户编码")   
    private Integer accountCode;
    /**
     * 商家账户类型
     */
    @ApiModelProperty("商家账户类型")   
    private String merAccountTypeCode;
    /**
     * 余额
     */
    @ApiModelProperty("余额")   
    private BigDecimal accountBalance;
    /**
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
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

//以下为列明常量

    /**
    * 自增id
    */
    public static final String ID = "id";
    /**
    * 账户编码
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 商家账户类型
    */
    public static final String MER_ACCOUNT_TYPE_CODE = "mer_account_type_code";
    /**
    * 余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 删除标志  1-删除、0-未删除
    */
    public static final String DELETED = "deleted";
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