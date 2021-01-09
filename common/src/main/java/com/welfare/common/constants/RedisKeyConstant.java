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
   *  员工账号申请key
   */
  public static final String ACCOUNT_DEPOSIT_APPLY__ID = "account_deposit_apply:id:";

  public static final String GENERATE_BARCODE_SALT_LOCK = "e-welfare_barcode_salt_lock";
  /**
   * 操作MerchantAccountType表的更新需要的锁key前缀
   */
  public static final String MER_ACCOUNT_TYPE_OPERATE = "e-welfare_mer_account_type_operate";

  public static String buidKey(String perfix, String... part) {
    return StringUtils.removeEnd(perfix, ":").concat(":").concat(StringUtils.join(part, ":"));
  }

}