package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
public class MerchantAddressDTO {
  /**
   * id
   */
  @ApiModelProperty("id")
  private Long id;
  /**
   * 地址名称
   */
  @ApiModelProperty("地址名称")
  private String addressName;
  /**
   * 详细地址
   */
  @ApiModelProperty("详细地址")
  private String address;
  /**
   * 地址类型
   */
  @ApiModelProperty("地址类型")
  private String addressType;
  /**
   * 状态
   */
  @ApiModelProperty("状态")
  private Integer status;
  /**
   * 创建人
   */
  @ApiModelProperty("创建人")
  private String createUser;
  /**
   * 创建日期
   */
  @ApiModelProperty("创建日期")
  private Date createTime;
  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  private String updateUser;
  /**
   * 更新日期
   */
  @ApiModelProperty("更新日期")
  private Date updateTime;
  /**
   * 关联类型
   */
  @ApiModelProperty("关联类型")
  private String relatedType;
  /**
   * 关联id
   */
  @ApiModelProperty("关联id")
  private Long relatedId;

}
