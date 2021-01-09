package com.welfare.service;

import com.welfare.persist.entity.AccountAmountType;
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
}