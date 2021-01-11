package com.welfare.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  11:43 AM
 */
@Data
@NoArgsConstructor
public class MerchantUserInfo {

  /**
   * 商户编码
   */
  private String merchantCode;

  /**
   * 商户用户名称
   */
  private String username;

  /**
   * 商户用户账号
   */
  private String userCode;
}