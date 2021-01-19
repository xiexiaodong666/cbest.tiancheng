package com.welfare.persist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 15:26
 */
@Data
public class AccountPageDTO {
  private Long id;
  private String merName;
  private String merCode;
  private String accountName;
  @JsonSerialize(using = ToStringSerializer.class)
  private String accountCode;
  private String accountStatus;
  private String accountStatusString;
  private String departmentCode;
  private String departmentName;
  private String accountTypeName;
  private BigDecimal maxQuota;
  private BigDecimal surplusQuota;
  private String cardId;
  private BigDecimal accountBalance;
  private Integer binding;
  private String phone;
}
