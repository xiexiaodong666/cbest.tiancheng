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
 * (sequence)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sequence")
@ApiModel("")
public class Sequence extends Model<Sequence> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @ApiModelProperty("pk")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 序列类型
     */
    @ApiModelProperty("序列类型")   
    private String sequenceType;
    /**
     * prefix
     */
    @ApiModelProperty("prefix")   
    private String prefix;
    /**
     * 序列号
     */
    @ApiModelProperty("序列号")   
    private Long sequenceNo;
    /**
     * minSequence
     */
    @ApiModelProperty("minSequence")   
    private Long minSequence;
    /**
     * maxSequence
     */
    @ApiModelProperty("maxSequence")   
    private Long maxSequence;
    /**
     * handlerForMax
     */
    @ApiModelProperty("handlerForMax")   
    private String handlerForMax;
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
     * 删除标记
     */
    @ApiModelProperty("删除标记") @TableLogic   
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
    * pk
    */
    public static final String ID = "id";
    /**
    * 序列类型
    */
    public static final String SEQUENCE_TYPE = "sequence_type";
    /**
    * 
    */
    public static final String PREFIX = "prefix";
    /**
    * 序列号
    */
    public static final String SEQUENCE_NO = "sequence_no";
    /**
    * 
    */
    public static final String MIN_SEQUENCE = "min_sequence";
    /**
    * 
    */
    public static final String MAX_SEQUENCE = "max_sequence";
    /**
    * 
    */
    public static final String HANDLER_FOR_MAX = "handler_for_max";
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
    * 删除标记
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}