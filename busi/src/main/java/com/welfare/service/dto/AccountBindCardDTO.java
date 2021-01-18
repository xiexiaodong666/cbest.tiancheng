package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 17:45
 */
@Data
@NoArgsConstructor
public class AccountBindCardDTO extends BaseRowModel {
  @ExcelProperty(value = "员工账号", index = 0)
  private String accountCode;
  @ExcelProperty(value = "卡号", index = 1)
  private String cardId;
}
