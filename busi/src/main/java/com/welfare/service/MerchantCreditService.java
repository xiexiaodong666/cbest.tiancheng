package com.welfare.service;


import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商户额度信服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantCreditService {


  /**
   * 通过商户编码查询额度信息
   * @param merCode
   * @return
   */
  MerchantCredit getByMerCode(String merCode);



  /**
   * 减少额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   * @return
   */
  List<MerchantAccountOperation> decreaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo);

  /**
   * 增加额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   */
  void increaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo);
}
