package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 商户部门(department)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 14:23:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("department")
@ApiModel("商户部门")
public class Department extends Model<Department> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")   
    private String departmentName;
    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")   
    private String departmentCode;
    /**
     * 部门父级
     */
    @ApiModelProperty("部门父级")   
    private String departmentParent;
    /**
     * 部门层级
     */
    @ApiModelProperty("部门层级")   
    private Integer departmentLevel;
    /**
     * 部门路径
     */
    @ApiModelProperty("部门路径")   
    private String departmentPath;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic @TableField  
    private Boolean deleted;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    private Integer version;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")   
    private String externalCode;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 部门名称
    */
    public static final String DEPARTMENT_NAME = "department_name";
    /**
    * 部门编码
    */
    public static final String DEPARTMENT_CODE = "department_code";
    /**
    * 部门父级
    */
    public static final String DEPARTMENT_PARENT = "department_parent";
    /**
    * 部门层级
    */
    public static final String DEPARTMENT_LEVEL = "department_level";
    /**
    * 部门路径
    */
    public static final String DEPARTMENT_PATH = "department_path";
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
    /**
    * 外部编码
    */
    public static final String EXTERNAL_CODE = "external_code";

}