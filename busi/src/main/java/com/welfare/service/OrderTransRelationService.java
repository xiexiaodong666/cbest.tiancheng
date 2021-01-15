package com.welfare.service;

import com.welfare.common.constants.WelfareConstant;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/15  12:10 AM
 */
public interface OrderTransRelationService {

  /**
   * 保存新的OrderTransRelation
   * @param orderId
   * @param transNo
   * @param transType
   */
  void saveNewTransRelation(String orderId, String transNo, WelfareConstant.TransType transType);
}