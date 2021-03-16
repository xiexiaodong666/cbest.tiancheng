package com.welfare.service.remote.service;

import com.welfare.service.remote.entity.request.WoLifeAccountDeductionDataRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffDataRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/16 5:18 PM
 */
public interface WoLifeFeignService {

  /**
   * 账户余额查询
   */
  WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(String phone);

  /**
   * 账户扣款
   */
  WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(
      String phone, WoLifeAccountDeductionDataRequest data);

  /**
   * 退款销账
   */
  WoLifeBasicResponse refundWriteOff(String phone, WoLifeRefundWriteOffDataRequest data);

}
