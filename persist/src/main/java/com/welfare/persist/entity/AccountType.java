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
 * 员工类型(account_type)实体类
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-15 15:14:23
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_type")
@ApiModel("员工类型")
public class AccountType extends Model<AccountType> implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @ApiModelProperty("id")
  @JsonSerialize(using = ToStringSerializer.class)
  @TableId
  private Long id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 类型编码
   */
  @ApiModelProperty("类型编码")
  private String typeCode;
  /**
   * 类型名称
   */
  @ApiModelProperty("类型名称")
  private String typeName;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private String remark;
  /**
   * 删除标志
   */
  @ApiModelProperty("删除标志")
  @TableLogic
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
  @ApiModelProperty("版本")
  @Version
  @TableField(fill = FieldFill.INSERT)
  private Integer version;

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
   * 类型编码
   */
  public static final String TYPE_CODE = "type_code";
  /**
   * 类型名称
   */
  public static final String TYPE_NAME = "type_name";
  /**
   * 备注
   */
  public static final String REMARK = "remark";
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