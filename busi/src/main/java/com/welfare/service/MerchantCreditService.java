package com.welfare.service;


import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.MerchantCredit;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 商户额度信服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantCreditService {

  /**
   * 减少申请充值额度
   * 当前额度小于减少的额度 返回 0
   * @param increaseLimit
   * @param id
   * @return
   */
  int decreaseRechargeLimit(BigDecimal increaseLimit, Long id);

  /**
   * 通过商户编码查询额度信息
   * @param merCode
   * @return
   */
  MerchantCredit getByMerCode(String merCode);

  /**
   * 更新充值额度
   * @param merchantCredit
   * @param amount
   */
  void updateMerchantCredit(MerchantCredit merchantCredit,BigDecimal amount);

}
