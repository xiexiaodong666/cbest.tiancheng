package com.welfare.service.remote.entity.response;

import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 3:33 PM
 */
@Data
public class WoLifeGetUserMoneyResponse {

  /**
   * 当前积分
   */
  private String integral;

  /**
   * 冻结积分
   */
  private String frozenIntegral;

  /**
   * 当前可用积分
   */
  private String availableIntegral;

  /**
   * 当前余额
   */
  private String money;

  /**
   * 冻结余额
   */
  private String frozenMoney;

  /**
   * 实际可用余额
   */
  private String availableBalance;

  /**
   * 当前福利积分
   */
  private String welfareIntegral;

  /**
   * 冻结福利积分
   */
  private String frozenWelfareIntegral;

  /**
   * 当前可用福利积分
   */
  private String availableWelfareIntegral;

  /**
   * 状态，默认为”0”
   */
  private String status;
}
