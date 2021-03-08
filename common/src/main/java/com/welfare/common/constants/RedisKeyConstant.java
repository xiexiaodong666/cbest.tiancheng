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
  public static final String ACCOUNT_DEPOSIT_APPLY_SAVE_REQUEST_ID = "account_deposit_apply_save_request_id:";

  /**
   *  员工账号申请key
   */
  public static final String ACCOUNT_DEPOSIT_APPLY__ID = "account_deposit_apply:id:";

  public static final String GENERATE_BARCODE_SALT_LOCK = "e-welfare_barcode_salt_lock";
  /**
   * 操作MerchantAccountType表的更新需要的锁key前缀
   */
  public static final String MER_ACCOUNT_TYPE_OPERATE = "e-welfare_mer_account_type_operate";

  /**
   * 新增TempAccountDepositApply需要的锁key前缀
   */
  public static final String TEMP_ACCOUNT_DEPOSIT_APPLY_SAVE = "temp_account_deposit_apply:save:";

  /**
   * 新增商户额度申请request id
   */
  public static final String MERCHANT_CREDIT_APPLY_REQUEST_ID = "merchant_credit_apply_request_id:";

  /**
   * 操作商户额度申请key前缀
   */
  public static final String MERCHANT_CREDIT_APPLY= "merchant_credit_apply:";

  /**
   * 生成序列号
   */
  public static final String SEQUENCE_GENERATE = "sequence_generate:";
  /**
   * 操作AccountAmountType表的更新需要的锁key前缀
   */
  public static final String ACCOUNT_AMOUNT_TYPE_OPERATE = "e-welfare_account_amount_type_operate:";

  /**
   * 调用恢复剩余剩余信用额度接口 请求id的key前缀
   */
  public static final String RESTORE_REMAINING_LIMIT_REQUEST_ID = "e-welfare_restore_remaining_limit_request_id:";

  /**
   * 生产员工授信结算单需要用到的锁key前缀
   */
  public static final String BUILD_EMPLOYEE_SETTLE = "e-welfare_build_employee_settle:";

  /**
   * 结算员工授信需要用到的锁key前缀
   */
  public static final String FINISH_EMPLOYEE_SETTLE = "e-welfare_finish_employee_settle:";

  /**
   * 拉取员工授信流水明细到结算明细表时需要用到的锁key前缀
   */
  public static final String PULL_EMPLOYEE_SETTLE_DETAIL = "e-welfare_pull_employee_settle_detail:";

  public static String buidKey(String perfix, String... part) {
    return StringUtils.removeEnd(perfix, ":").concat(":").concat(StringUtils.join(part, ":"));
  }

}