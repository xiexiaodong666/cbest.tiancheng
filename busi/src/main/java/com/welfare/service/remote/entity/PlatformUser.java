package com.welfare.service.remote.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 10:07 AM
 */
@Data
public class PlatformUser {


  private Long id;
  private String name;
  private String username;
  private String initPassword;
  private String merchantName;
  private String merchantCode;
  /**
   * 1 正常，0 锁定， 2 删除
   */
  private Integer status;
  private String remark;
  private String createdBy;
  private String updatedBy;
  private Date createdAt;
}
