package com.welfare.persist.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 17:47
 */
@Data
public class AccountBillDetailMapperDTO {
  private String storeName;
  private String transType;
  private String transTypeString;
  private BigDecimal transAmount;
  private Date createTime;
  private BigDecimal  accountBalance;
  private String accountTypeName;
}
