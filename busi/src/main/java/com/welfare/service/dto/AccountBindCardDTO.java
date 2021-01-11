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
  @ExcelProperty(value = "申请编码", index = 0)
  private String applyCode;
  @ExcelProperty(value = "卡号", index = 1)
  private String cardId;
  @ExcelProperty(value = "卡类型", index = 2)
  private String cardType;
  @ExcelProperty(value = "卡状态", index = 3)
  private String cardStatus;
  @ExcelProperty(value = "员工账号", index = 4)
  private String accountCode;
  @ExcelProperty(value = "磁条号", index = 5)
  private String magneticStripe;
  @ExcelProperty(value = "创建人", index = 6)
  private String createUser;
}
