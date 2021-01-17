package com.welfare.service;

import com.welfare.persist.entity.MerchantBillDetail;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
public interface MerchantBillDetailService {

  /**
   * 通过交易类型和流水号查询流水记录
   * @param transNO
   * @param tranTsype
   * @return
   */
  List<MerchantBillDetail> findByTransNoAndTransType(String transNO, String tranTsype);
}
