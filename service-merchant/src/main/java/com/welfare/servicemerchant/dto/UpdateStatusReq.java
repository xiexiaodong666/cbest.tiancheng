package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/18 11:33
 */
@Data
@ApiModel("状态修改以及删除Req")
public class UpdateStatusReq implements Serializable {
  @ApiModelProperty("ID(员工账号,类型,消费场景)")
  private String id;
  @ApiModelProperty("账号状态(1正常2禁用)")
  private Integer accountStatus;
  @ApiModelProperty("消费场景状态(1正常 2禁用)")
  private Integer consumeSceneStatus;
}
