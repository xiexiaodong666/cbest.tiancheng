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
 * 卡信息(card_info)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("card_info")
@ApiModel("卡信息")
public class CardInfo extends Model<CardInfo> implements Serializable {
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
     * 卡号
     */
    @ApiModelProperty("卡号")   
    private String cardId;
    /**
     * 卡状态
     */
    @ApiModelProperty("卡状态")   
    private Integer cardStatus;
    @ApiModelProperty("启用状态")
    private Integer enabled;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
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
     * 员工账号
     */
    @ApiModelProperty("员工账号")   
    private Long accountCode;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;
    /**
     * 磁条号
     */
    @ApiModelProperty("磁条号")   
    private String magneticStripe;
    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")   
    private Date writtenTime;
    /**
     * 绑定时间
     */
    @ApiModelProperty("绑定时间")   
    private Date bindTime;

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
    * 卡号
    */
    public static final String CARD_ID = "card_id";

    /**
    * 卡状态
    */
    public static final String CARD_STATUS = "card_status";
    /**
    * 删除标志
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
    * 员工账号
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 磁条号
    */
    public static final String MAGNETIC_STRIPE = "magnetic_stripe";
    /**
    * 入库时间
    */
    public static final String WRITTEN_TIME = "written_time";
    /**
    * 绑定时间
    */
    public static final String BIND_TIME = "bind_time";

}