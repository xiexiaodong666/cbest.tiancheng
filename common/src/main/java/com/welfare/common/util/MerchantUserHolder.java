package com.welfare.common.util;

import com.welfare.common.domain.MerchantUser;


/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  11:41 AM
 */
public class MerchantUserHolder {

  private static final ThreadLocal<MerchantUser> DEPT_ID_LOCAL = new ThreadLocal<>();

  public static MerchantUser getDeptIds() {
    return DEPT_ID_LOCAL.get();
  }

  public static void setDeptIds(MerchantUser merchantUser) {
    DEPT_ID_LOCAL.set(merchantUser);
  }

  public static void release() {
    DEPT_ID_LOCAL.remove();
  }
}