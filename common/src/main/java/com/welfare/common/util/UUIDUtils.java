package com.welfare.common.util;

import java.util.UUID;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/12  3:52 PM
 */
public class UUIDUtils {

  //得到32位的uuid
  public static String getUUID32(){
    return UUID.randomUUID().toString().replace("-", "").toLowerCase();
  }
}