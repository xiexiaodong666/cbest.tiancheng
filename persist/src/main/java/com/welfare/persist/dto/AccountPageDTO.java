package com.welfare.persist.dto;

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
  private String accountName;
  private String accountCode;
  private String accountStatus;
  private String departmentCode;
  private String departmentName;
  private String accountTypeName;
  private BigDecimal maxQuota;
  private BigDecimal surplusQuota;
  private String cardId;
  private BigDecimal accountBalance;
  private Integer active;
}
