package com.welfare.persist.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
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
 * 文件模板(file_template)实体类
 *
 * @author hao.yin
 * @since 2021-01-18 17:24:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("file_template")
@ApiModel("文件模板")
public class FileTemplate extends Model<FileTemplate> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")   
    private String fileType;
    /**
     * 文件下载地址
     */
    @ApiModelProperty("文件下载地址")   
    private String url;
    /**
     * 删除标志
     */
    @ApiModelProperty("deleted") @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
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

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 文件类型
    */
    public static final String FILE_TYPE = "file_type";
    /**
    * 文件下载地址
    */
    public static final String URL = "url";
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