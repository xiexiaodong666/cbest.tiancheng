package com.welfare.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 11:40
 */
@Data
public class AccountConsumeSceneStoreRelationReq implements Serializable {
  /**
   * id
   */
  @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;
  /**
   * accountConsumeSceneId
   */
  @ApiModelProperty("accountConsumeSceneId")
  private Long accountConsumeSceneId;
  /**
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  private String storeCode;
  /**
   * 消费方式
   */
  @ApiModelProperty("消费方式")
  private String sceneConsumType;
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

  /**
   * 删除标志  1-删除、0-未删除
   */
  @ApiModelProperty("删除标志  1-删除、0-未删除")
  private Integer deleted;

}
