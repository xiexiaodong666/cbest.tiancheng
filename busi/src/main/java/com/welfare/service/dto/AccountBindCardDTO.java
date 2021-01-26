package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 17:45
 */
@Data
@NoArgsConstructor
public class AccountBindCardDTO extends BaseRowModel {
  @ExcelProperty(value = "手机号", index = 0)
  @NotBlank(message = "手机号不能为空")
  //@NotDuplicate
  private String phone;
  @ExcelProperty(value = "卡号", index = 1)
  @NotBlank(message = "卡号不能为空")
  private String cardId;
}
