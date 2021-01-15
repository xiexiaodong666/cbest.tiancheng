package com.welfare.service.remote.entity;

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
}
