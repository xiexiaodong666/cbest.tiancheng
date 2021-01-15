package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 地址信息(merchant_address)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_address")
@ApiModel("地址信息")
public class MerchantAddress extends Model<MerchantAddress> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 地址名称
     */
    @ApiModelProperty("地址名称")   
    private String addressName;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")   
    private String address;
    /**
     * 地址类型
     */
    @ApiModelProperty("地址类型")   
    private String addressType;
    /**
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer status;
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
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;
    /**
     * 关联类型
     */
    @ApiModelProperty("关联类型")   
    private String relatedType;
    /**
     * 关联id
     */
    @ApiModelProperty("关联id")   
    private Long relatedId;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 地址名称
    */
    public static final String ADDRESS_NAME = "address_name";
    /**
    * 详细地址
    */
    public static final String ADDRESS = "address";
    /**
    * 地址类型
    */
    public static final String ADDRESS_TYPE = "address_type";
    /**
    * 状态
    */
    public static final String STATUS = "status";
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
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 关联类型
    */
    public static final String RELATED_TYPE = "related_type";
    /**
    * 关联id
    */
    public static final String RELATED_ID = "related_id";

}