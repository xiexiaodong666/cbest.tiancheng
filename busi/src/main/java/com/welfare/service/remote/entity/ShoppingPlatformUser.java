package com.welfare.service.remote.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 10:44 AM
 */
@Data
public class ShoppingPlatformUser {

  @ExcelProperty(value ="id")
  private Long id;
  @ExcelProperty(value ="姓名")
  private String name;
  @ExcelProperty(value ="用户名")
  private String username;
  @ExcelProperty(value ="初始密码")
  private String init_password;
  @ExcelProperty(value ="商户名称")
  private String merchant_name;
  @ExcelProperty(value ="商户代码")
  private String merchant_code;
  /**
   * 1 正常，0 锁定， 2 删除
   */
  @ExcelProperty(value ="状态 1 正常，0 锁定")
  private Integer status;
  @ExcelProperty(value ="备注")
  private String remark;
  @ExcelProperty(value ="创建人")
  private String created_by;
  @ExcelProperty(value ="修改人")
  private String updated_by;
  @ExcelProperty(value ="创建时间")
  private Date created_at;
}
