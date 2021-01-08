package com.welfare.common.constants;

import org.apache.commons.lang.StringUtils;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  5:11 PM
 */
public class RedisKeyConstant {

  /**
   * 新增员工账号申请request id
   */
  public static final String ACCOUNT_DEPOSIT_APPLY_SAVE_REQUEST_ID = "account_deposit_apply:save:request_id:";

  /**
   *  审批员工账号申请request id
   */
  public static final String ACCOUNT_DEPOSIT_APPLY_APPROVAL_REQUEST_ID = "account_deposit_apply:approval:request_id:";

  public static String buidKey(String perfix, String... part) {
    return StringUtils.removeEnd(perfix, ":").concat(":").concat(StringUtils.join(part, ":"));
  }

}