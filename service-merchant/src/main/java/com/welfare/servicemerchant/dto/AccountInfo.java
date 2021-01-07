package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/6  5:28 PM
 */
@Data
@NoArgsConstructor
public class AccountInfo {

  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  private String accountName;
}