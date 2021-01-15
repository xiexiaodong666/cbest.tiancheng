package com.welfare.common.util;

import com.welfare.common.domain.AccountUserInfo;


/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  11:41 AM
 */
public class AccountUserHolder {

  private static final ThreadLocal<AccountUserInfo> accountUserInfoThreadLocal = new ThreadLocal<>();

  public static AccountUserInfo getAccountUser() {
    return accountUserInfoThreadLocal.get();
  }

  public static void setAccountUser(AccountUserInfo accountUser) {
    accountUserInfoThreadLocal.set(accountUser);
  }

  public static void release() {
    accountUserInfoThreadLocal.remove();
  }
}