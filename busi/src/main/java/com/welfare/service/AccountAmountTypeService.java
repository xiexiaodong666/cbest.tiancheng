package com.welfare.service;

import com.welfare.persist.entity.AccountAmountType;
import com.welfare.service.dto.Deposit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * 员工福利余额服务
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:28 PM
 */
public interface AccountAmountTypeService {

  /**
   * 批量新增或修改员工福利余额额度
   * @param list
   * @return
   */
  int batchSaveOrUpdate(List<AccountAmountType> list);

  /**
   * 获取唯一一条accountAmountType
   * @param accountCode
   * @param accountTypeCode
   * @return
   */
  AccountAmountType queryOne(String accountCode,String accountTypeCode);

  /**
   * 更新账户accountAmountType
   * @param deposit
   */
  void updateAccountAmountType(Deposit deposit);

  /**
   * 查询指定account的授信额度
   * @param accountCode
   * @return
   */
  AccountAmountType querySurplusQuota(String accountCode);
}