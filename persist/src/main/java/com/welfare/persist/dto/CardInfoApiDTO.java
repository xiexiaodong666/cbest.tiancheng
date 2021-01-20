package com.welfare.persist.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/17 9:59 PM
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("卡信息")
public class CardInfoApiDTO {
  /**
   * id
   */
  @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
  private Long id;
  /**
   * 申请编码
   */
  @ApiModelProperty("申请编码")
  private String applyCode;
  /**
   * 卡号
   */
  @ApiModelProperty("卡号")
  private String cardId;

  /**
   * 卡片名称
   */
  @ApiModelProperty("卡片名称")
  private String cardName;

  /**
   * 卡类型
   */
  @ApiModelProperty("卡类型")
  private String cardType;
  /**
   * 卡状态
   */
  @ApiModelProperty("卡状态")
  private Integer cardStatus;
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
   * 员工账号
   */
  @ApiModelProperty("员工账号")
  private Long accountCode;
  /**
   * 版本
   */
  @ApiModelProperty("版本")  @Version
  @TableField(fill = FieldFill.INSERT)
  private Integer version;
  /**
   * 磁条号
   */
  @ApiModelProperty("磁条号")
  private String magneticStripe;
  /**
   * 入库时间
   */
  @ApiModelProperty("入库时间")
  private Date writtenTime;
  /**
   * 绑定时间
   */
  @ApiModelProperty("绑定时间")
  private Date bindTime;
  /**
   * 卡介质
   */
  @ApiModelProperty("卡介质")
  private String cardMedium;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;
}
