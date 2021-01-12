package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 员工消费场景配置(account_consume_scene)实体类
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-09 15:13:38
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_consume_scene")
@ApiModel("员工消费场景配置")
public class AccountConsumeScene extends Model<AccountConsumeScene> implements Serializable {

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
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  private String accountTypeCode;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
  /**
   * 状态
   */
  @ApiModelProperty("状态")
  private Integer status;
  /**
   * 创建人
   */
  @ApiModelProperty("创建人")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String createUser;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date createTime;
  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateUser;
  /**
   * 更新时间
   */
  @ApiModelProperty("更新时间")
  @TableField(update = "now()")
  private Date updateTime;
  /**
   * 删除标志  1-删除、0-未删除
   */
  @ApiModelProperty("删除标志  1-删除、0-未删除")
  @TableLogic
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Boolean deleted;
  /**
   * 版本
   */
  @ApiModelProperty("版本")
  @Version
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Integer version;

  @ApiModelProperty("同步状态")
  private Integer syncStatus;

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
   * 员工类型编码
   */
  public static final String ACCOUNT_TYPE_CODE = "account_type_code";
  /**
   * 备注
   */
  public static final String REMARK = "remark";
  /**
   * 状态
   */
  public static final String STATUS = "status";
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
   * 删除标志  1-删除、0-未删除
   */
  public static final String DELETED = "deleted";
  /**
   * 版本
   */
  public static final String VERSION = "version";
  /**
   * 同步状态
   */
  public static final String SYNC_STATUS="sync_status";

}