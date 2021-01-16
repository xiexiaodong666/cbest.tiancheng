package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字典(dict)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("dict")
@ApiModel("字典")
public class Dict extends Model<Dict> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 码表类型
     */
    @ApiModelProperty("码表类型")   
    private String dictType;
    /**
     * 编码
     */
    @ApiModelProperty("编码")   
    private String dictCode;
    /**
     * 名称
     */
    @ApiModelProperty("名称")   
    private String dictName;
    /**
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer status;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
    /**
     * 顺序
     */
    @ApiModelProperty("顺序")   
    private Integer sort;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 码表类型
    */
    public static final String DICT_TYPE = "dict_type";
    /**
    * 编码
    */
    public static final String DICT_CODE = "dict_code";
    /**
    * 名称
    */
    public static final String DICT_NAME = "dict_name";
    /**
    * 状态
    */
    public static final String STATUS = "status";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 顺序
    */
    public static final String SORT = "sort";

}