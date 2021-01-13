package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 10:56
 */
@Data
@NoArgsConstructor
public class AccountUploadDTO extends BaseRowModel {

  @ExcelProperty(value = "商户编码", index = 0)
  private String merCode;
  @ExcelProperty(value = "员工名称", index = 1)
  private String accountName;
  @ExcelProperty(value = "手机号", index = 2)
  private String phone;
  @ExcelProperty(value = "账号状态", index = 3)
  private String accountStatus;
  @ExcelProperty(value = "员工类型编码", index = 4)
  private String accountTypeCode;
  @ExcelProperty(value = "所属部门", index = 5)
  private String storeCode;
  @ExcelProperty(value = "创建人姓名", index = 6)
  private String createUser;
}
