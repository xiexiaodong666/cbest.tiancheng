package com.welfare.persist.dto;

import com.welfare.persist.entity.Account;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 14:34
 */
@Data
public class AccountSyncDTO extends Account {
  private String merchantId;
}
