package com.welfare.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/21 11:09
 */
public class AccountUtil {

  private static String regex = "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";//"^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

  public static boolean validPhone(String phone){
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(phone);
    return m.matches();
  }
  public static <T> List<List<T>> averageAssign(List<T> source) {
    int n = source.size() % 500 == 0 ? source.size() / 500 : source.size() / 500 + 1;
    List<List<T>> result = new ArrayList<List<T>>();
    //(先计算出余数)
    int remainder = source.size() % n;
    //然后是商
    int number = source.size() / n;
    //偏移量
    int offset = 0;
    for (int i = 0; i < n; i++) {
      List<T> value = null;
      if (remainder > 0) {
        value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
        remainder--;
        offset++;
      } else {
        value = source.subList(i * number + offset, (i + 1) * number + offset);
      }
      result.add(value);
    }
    return result;
  }

  /**
   * 手机号脱敏
   * @param phoneNumber
   * @return
   */
  public static String desensitizedPhoneNumber(String phoneNumber){
    if(StringUtils.isNotEmpty(phoneNumber)){
      phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
    }
    return phoneNumber;
  }
}
