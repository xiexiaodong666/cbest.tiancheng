package com.welfare.persist.dto;

import com.welfare.persist.entity.Account;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 14:34
 */
@Data
public class AccountSyncDTO extends Account {
  private String merchantId;

  @ApiModelProperty("员工类型名称")
  private String roleName;

  @ApiModelProperty("机构编码")
  private String departmentCode;

  @ApiModelProperty("机构名称")
  private String departmentName;
}
