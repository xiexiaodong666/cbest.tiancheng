package com.welfare.persist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 17:04
 */
@Data
@ApiModel("员工消费场景列表DTO")
public class AccountConsumeScenePageDTO {

  /**
   * ID
   */
  @ApiModelProperty("ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;
  /**
   * 门店以及编码,逗号分隔
   */
  @ApiModelProperty("门店以及编码,逗号分隔")
  private String storeInfo;
  /**
   * 员工类型消费场景状态
   */
  @ApiModelProperty("员工类型消费场景状态")
  private Integer status;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
}
