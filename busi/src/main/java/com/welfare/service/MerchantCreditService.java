package com.welfare.service;


import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.dto.DecreaseMerchantCredit;
import com.welfare.service.dto.MerchantExtendDTO;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import com.welfare.service.operator.merchant.AbstractMerAccountTypeOperator;
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

  boolean init(String merCode);


  /**
   * 减少额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   * @param transType
   * @return
   */
  List<MerchantAccountOperation> decreaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo, String transType);


  /**
   * 批量减少额度
   * @param decreaseMerchantCredits
   * @return
   */
  void batchDecreaseLimit(List<DecreaseMerchantCredit> decreaseMerchantCredits);

  /**
   * 操作商户金额
   * @param merCode
   * @param amount
   * @param transNo
   * @param merAccountTypeOperator
   * @param transType
   * @return
   */
  List<MerchantAccountOperation> doOperateAccount(MerchantCredit merCode,
                                                  BigDecimal amount,
                                                  String transNo,
                                                  AbstractMerAccountTypeOperator merAccountTypeOperator, String transType);

  /**
   * 增加额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   * @param transType
   * @return
   */
  List<MerchantAccountOperation> increaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo, String transType);

  /**
   * 设置额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   */
  void setAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo);

  /**
   * 设置批发额度
   * @param merCode
   * @param merCreditType
   * @param amount
   * @param transNo
   */
  void setWholesaleLimit(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo);

  /**
   * 恢复剩余信用额度
   * @param req
   */
  void restoreRemainingLimit(RestoreRemainingLimitReq req);

}
