package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
 * (file_universal_storage)实体类
 *
 * @author Yuxiang Li
 * @since 2021-02-01 11:20:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("file_universal_storage")
@ApiModel("")
public class FileUniversalStorage extends Model<FileUniversalStorage> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ApiModelProperty("自增id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 文件存储类型
     */
    @ApiModelProperty("文件存储类型")   
    private String type;
    /**
     * s3存储key
     */
    @ApiModelProperty("s3存储key")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String imgKey;
    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String url;
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
     * 更新日期
     */
    @ApiModelProperty("更新日期")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    @TableField(fill = FieldFill.UPDATE)
	private String updateUser;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;

//以下为列明常量

    /**
    * 自增id
    */
    public static final String ID = "id";
    /**
    * 文件存储类型
    */
    public static final String TYPE = "type";
    /**
    * s3存储key
    */
    public static final String IMG_KEY = "img_key";
    /**
    * 文件路径
    */
    public static final String URL = "url";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建日期
    */
    public static final String CREATE_TIME = "create_time";
    /**
    * 更新日期
    */
    public static final String UPDATE_TIME = "update_time";
    /**
    * 更新人
    */
    public static final String UPDATE_USER = "update_user";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";

}