package com.welfare.common.util;

import com.welfare.common.domain.UserInfo;
import com.welfare.common.domain.MerchantUserInfo;

import java.util.Objects;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/9 11:59 AM
 */
public class UserInfoHolder {

  private static final ThreadLocal<UserInfo> API_USER_INFO_LOCAL = new ThreadLocal<>();

  public static UserInfo getUserInfo() {
    UserInfo userInfo = API_USER_INFO_LOCAL.get();
    if(Objects.isNull(userInfo)){
      MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
      if(Objects.isNull(merchantUser)){
        return UserInfo.anonymousUser();
      }else{
        return UserInfo.of(merchantUser);
      }
    }
    return API_USER_INFO_LOCAL.get();
  }

  public static void setApiUserInfoLocal(UserInfo merchantUser) {
    API_USER_INFO_LOCAL.set(merchantUser);
  }

  public static void release() {
    API_USER_INFO_LOCAL.remove();
  }
}
