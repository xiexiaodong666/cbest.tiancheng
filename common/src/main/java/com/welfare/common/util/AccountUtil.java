package com.welfare.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/21 11:09
 */
public class AccountUtil {
  private static String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

  public static boolean validPhone(String phone){
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(phone);
    return m.matches();
  }
}
