package com.welfare.service.remote.entity;

import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 10:44 AM
 */
@Data
public class ShoppingPlatformUser {
  private Long id;
  private String name;
  private String username;
  private String init_password;
  private String merchant_name;
  private String merchant_code;
  /**
   * 1 正常，0 锁定， 2 删除
   */
  private Integer status;
  private String remark;
  private String created_by;
  private String updated_by;
  private Date created_at;
}
