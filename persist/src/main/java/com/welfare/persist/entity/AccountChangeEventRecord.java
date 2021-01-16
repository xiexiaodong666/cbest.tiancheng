package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (account_change_event_record)实体类
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-15 15:14:22
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_change_event_record")
public class AccountChangeEventRecord extends Model<AccountChangeEventRecord> implements
    Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 自增ID
   */
  @ApiModelProperty("自增ID")
  @JsonSerialize(using = ToStringSerializer.class)
  @TableId(value = "id",type = IdType.AUTO )
  private Long id;
  /**
   * 员工账号Code
   */
  @ApiModelProperty("员工账号Code")
  private Long accountCode;
  /**
   * 变更类型
   */
  @ApiModelProperty("变更类型")
  private String changeType;
  /**
   * 变更类型名称
   */
  @ApiModelProperty("变更类型名称")
  private String changeValue;
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

//以下为列明常量

  /**
   * 自增ID
   */
  public static final String ID = "id";
  /**
   * 员工账号Code
   */
  public static final String ACCOUNT_CODE = "account_code";
  /**
   * 变更类型
   */
  public static final String CHANGE_TYPE = "change_type";
  /**
   * 变更类型名称
   */
  public static final String CHANGE_VALUE = "change_value";
  /**
   * 创建人
   */
  public static final String CREATE_USER = "create_user";
  /**
   * 创建时间
   */
  public static final String CREATE_TIME = "create_time";

}