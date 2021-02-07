package com.welfare.service.remote.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 10:57
 */
@Data
public class EmployerDTO {
  private String employerId;
  private String employerRole;
  private String partnerCode;
  private String merchantId;
  private String mobile;
  private String name;
  private String remark;
  private Integer status;
  @ApiModelProperty("员工类型名称")
  private String roleName;
  @ApiModelProperty("机构编码")
  private String departmentCode;
  @ApiModelProperty("机构名称")
  private String departmentName;
}
