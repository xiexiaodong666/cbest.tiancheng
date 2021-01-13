package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 9:09
 */
@Data
@ApiModel("员工类型model")
public class AccountTypeDTO {
  @ApiModelProperty("ID")
  private Long id;
  @ApiModelProperty("商户编码")
  private String merCode;
  @ApiModelProperty("员工类型编码")
  private String typeCode;
  @ApiModelProperty("员工类型名称")
  private String typeName;
  @ApiModelProperty("备注")
  private String remark;
}
