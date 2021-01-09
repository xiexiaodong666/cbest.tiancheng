package com.welfare.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/9 11:57 AM
 */
@Data
@NoArgsConstructor
public class ApiUserInfo {

  /**
   * 用户账号
   */
  private String userId;

  /**
   * 用户昵称
   */
  private String userName;

}
