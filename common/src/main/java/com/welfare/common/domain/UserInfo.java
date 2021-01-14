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
public class UserInfo {

  /**
   * 用户账号
   */
  private String userId;

  /**
   * 用户昵称
   */
  private String userName;

  /**
   * 商户编码
   */
  private String merchantCode;

  public static UserInfo anonymousUser(){
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId("anonymous");
    userInfo.setUserName("anonymous");
    userInfo.setMerchantCode("anonymous");
    return userInfo;
  }

  public static UserInfo of(MerchantUserInfo merchantUserInfo){
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(merchantUserInfo.getUserCode());
    userInfo.setUserName(merchantUserInfo.getUsername());
    userInfo.setMerchantCode(merchantUserInfo.getMerchantCode());
    return userInfo;
  }

}
