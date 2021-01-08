package com.welfare.servicemerchant.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 11:49
 */
@Data
public class AccountConsumeSceneReq implements Serializable {
  /**
   * id
   */
  @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
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
  private String accountTypeId;
  /**
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  private String storeCode;
  /**
   * 消费方式
   */
  @ApiModelProperty("消费方式")
  private String consumType;
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
  private String createUser;
  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  private String updateUser;

  /**
   * 删除标志  1-删除、0-未删除
   */
  @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic
  private Boolean deleted;

}
