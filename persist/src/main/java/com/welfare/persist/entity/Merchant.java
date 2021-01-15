package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商户信息(merchant)实体类
 *
 * @author hao.yin
 * @since 2021-01-14 11:03:55
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
    @TableId(type = IdType.AUTO)
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
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String createUser;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
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
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer status;

    @ApiModelProperty("删除标记")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
    * 版本
    */
    public static final String VERSION = "version";

}