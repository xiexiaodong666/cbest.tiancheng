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
 * (sequence)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-11 19:54:01
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
    @ApiModelProperty("前缀")
    private String prefix;
    /**
     * 序列号
     */
    @ApiModelProperty("序列号")   
    private Long sequenceNo;
    @ApiModelProperty("最小序列号")
    private Long minSequence;
    @ApiModelProperty("最大序列号")
    private Long maxSequence;

    @ApiModelProperty("当达到最大序列号时的处理")
    private String handlerForMax;
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
     * 删除标记
     */
    @ApiModelProperty("删除标记") @TableLogic   
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
    * pk
    */
    public static final String ID = "id";
    /**
    * 序列类型
    */
    public static final String SEQUENCE_TYPE = "sequence_type";
    /**
    * 序列号
    */
    public static final String SEQUENCE_NO = "sequence_no";
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