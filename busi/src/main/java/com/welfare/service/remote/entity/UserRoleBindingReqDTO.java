package com.welfare.service.remote.entity;

import com.welfare.common.enums.ShoppingActionTypeEnum;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 17:19
 */
@Data
public class UserRoleBindingReqDTO implements Serializable {
  private ShoppingActionTypeEnum actionType;
  private List<UserRoleBinding> list;
  private String requestId;
  private String timestamp;
}
