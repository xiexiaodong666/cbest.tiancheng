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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (message_push_config)实体类
 *
 * @author Yuxiang Li
 * @since 2021-03-19 11:47:21
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("message_push_config")
@ApiModel("")
public class MessagePushConfig extends Model<MessagePushConfig> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 配置编码
     */
    @ApiModelProperty("配置编码")   
    private String configCode;
    /**
     * 配置名称
     */
    @ApiModelProperty("配置名称")   
    private String configName;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")   
    private String merCode;
    /**
     * 消息发送类型
     */
    @ApiModelProperty("消息发送类型")   
    private String targetType;
    /**
     * 模板类型
     */
    @ApiModelProperty("模板类型")   
    private String templateType;
    /**
     * 模板内容
     */
    @ApiModelProperty("模板内容")   
    private String templateContent;
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
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 配置编码
    */
    public static final String CONFIG_CODE = "config_code";
    /**
    * 配置名称
    */
    public static final String CONFIG_NAME = "config_name";
    /**
    * 商户名称
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 消息发送类型
    */
    public static final String TARGET_TYPE = "target_type";
    /**
    * 模板类型
    */
    public static final String TEMPLATE_TYPE = "template_type";
    /**
    * 模板内容
    */
    public static final String TEMPLATE_CONTENT = "template_content";
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
    * 版本
    */
    public static final String VERSION = "version";

}