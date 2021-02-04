package com.welfare.service.remote.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 17:18
 */
@Data
public class UserRoleBinding implements Serializable {
  private List<StoreBinding> bindings;
  private String employeeRole;
  private Boolean enabled;
  private String merchantCode;
}
