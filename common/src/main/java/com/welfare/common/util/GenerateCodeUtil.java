package com.welfare.common.util;

import java.util.UUID;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/9 7:12 PM
 */
public class GenerateCodeUtil {

  public static String getAccountIdByUUId() {
    int machineId = 1;//最大支持1-9个集群机器部署
    int hashCodeV = UUID.randomUUID().toString().hashCode();
    if (hashCodeV < 0) {//有可能是负数
      hashCodeV = -hashCodeV;
    }
    return machineId + String.format("%015d", hashCodeV);
  }
}
