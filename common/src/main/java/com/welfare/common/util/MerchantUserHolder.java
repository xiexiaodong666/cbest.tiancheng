package com.welfare.common.util;

import com.welfare.common.domain.MerchantUserInfo;


/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  11:41 AM
 */
public class MerchantUserHolder {

  private static final ThreadLocal<MerchantUserInfo> merchantUserInfoThreadLocal = new ThreadLocal<>();

  public static MerchantUserInfo getMerchantUser() {
    return merchantUserInfoThreadLocal.get();
  }

  public static void setMerchantUser(MerchantUserInfo merchantUser) {
    merchantUserInfoThreadLocal.set(merchantUser);
  }

  public static void release() {
    merchantUserInfoThreadLocal.remove();
  }
}