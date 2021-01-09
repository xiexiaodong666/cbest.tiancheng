package com.welfare.common.util;

import com.welfare.common.domain.ApiUserInfo;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/9 11:59 AM
 */
public class ApiUserHolder {

  private static final ThreadLocal<ApiUserInfo> API_USER_INFO_LOCAL = new ThreadLocal<>();

  public static ApiUserInfo getUserInfo() {
    return API_USER_INFO_LOCAL.get();
  }

  public static void setApiUserInfoLocal(ApiUserInfo merchantUser) {
    API_USER_INFO_LOCAL.set(merchantUser);
  }

  public static void release() {
    API_USER_INFO_LOCAL.remove();
  }
}
