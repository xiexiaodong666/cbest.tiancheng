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
 * 条码加盐信息(barcode_salt)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-08 18:06:33
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("barcode_salt")
@ApiModel("条码加盐信息")
public class BarcodeSalt extends Model<BarcodeSalt> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @ApiModelProperty("pk")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 有效期
     */
    @ApiModelProperty("有效期")   
    private String validPeriod;
    /**
     * 有效期数字表示
     */
    @ApiModelProperty("有效期数字表示")
    private Long validPeriodNumeric;
    /**
     * 加盐值
     */
    @ApiModelProperty("加盐值")   
    private Long saltValue;
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
     * 账户余额
     */
    @ApiModelProperty("账户余额")   
    private BigDecimal accountBalance;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic  
    private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    private Integer version;

//以下为列明常量

    /**
    * pk
    */
    public static final String ID = "id";
    /**
    * 有效期
    */
    public static final String VALID_PERIOD = "valid_period";
    /**
     * 有效期
     */
    public static final String VALID_PERIOD_NUMERIC = "valid_period_numeric";
    /**
    * 加盐值
    */
    public static final String SALT_VALUE = "salt_value";
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
    * 账户余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}