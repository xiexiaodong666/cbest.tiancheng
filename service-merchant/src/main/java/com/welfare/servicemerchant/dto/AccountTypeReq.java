package com.welfare.servicemerchant.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 11:34
 */
@Data
public class AccountTypeReq implements Serializable {
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
  private String remark;
  /**
   * 删除标志
   */
  @ApiModelProperty("删除标志") @TableLogic
  private Boolean deleted;
  /**
   * 创建人
   */
  @ApiModelProperty("创建人")
  private String createUser;
  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  private String updateUser;
}
