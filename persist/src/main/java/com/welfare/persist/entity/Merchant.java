package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户信息(merchant)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant")
@ApiModel("商户信息")
public class Merchant extends Model<Merchant> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")   
    private String merName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 商户类型
     */
    @ApiModelProperty("商户类型")   
    private String merType;
    /**
     * 身份属性
     */
    @ApiModelProperty("身份属性")   
    private String merIdentity;
    /**
     * 合作方式
     */
    @ApiModelProperty("合作方式")   
    private String merCooperationMode;
    /**
     * 员工自主充值
     */
    @ApiModelProperty("员工自主充值")   
    private String selfRecharge;
    /**
     * 备注
     */
    @ApiModelProperty("备注")   
    private String remark;

    /**
     * 员工卡消费明细门店显示
     */
    @ApiModelProperty("员工卡消费明细门店显示")
    private String billDetailShowStoreName;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT)
	private String createUser;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")   
    @TableField(fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    @TableField(fill = FieldFill.UPDATE)
	private String updateUser;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
    /**
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer status;
    /**
     * deleted
     */
    @ApiModelProperty("deleted") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;


//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 商户名称
    */
    public static final String MER_NAME = "mer_name";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 商户类型
    */
    public static final String MER_TYPE = "mer_type";
    /**
    * 身份属性
    */
    public static final String MER_IDENTITY = "mer_identity";
    /**
    * 合作方式
    */
    public static final String MER_COOPERATION_MODE = "mer_cooperation_mode";
    /**
    * 员工自主充值
    */
    public static final String SELF_RECHARGE = "self_recharge";
    /**
    * 备注
    */
    public static final String REMARK = "remark";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建日期
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
    * 状态
    */
    public static final String STATUS = "status";
    /**
    * 
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}