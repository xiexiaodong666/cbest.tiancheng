package com.welfare.service.remote.entity.response;

import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:13 AM
 */
@Data
public class WelfareMerChantConsumeDataBaiscResponse {

  /**
   * 商户代码
   */
  private String merCode;
  /**
   * 商户名称
   */
  private String merName;
  /**
   * 员工人数
   */
  private String userNum;
  /**
   * 商户余额
   */
  private String currentBalance;
  /**
   * 信用额度
   */
  private String creditLimit;

  /**
   * 剩余信用额度
   */
  private String remainingLimit;
  /**
   * 结算额度
   */
  private String settledMoney;
  /**
   * 待结算额度
   */
  private String unsettledMoney;
  /**
   * 业务类型
   */
  private String businessType;
  /**
   * 消费方式
   */
  private String consumeType;

  /**
   * 消费总金额
   */
  private String consumeMoney;
  /**
   * 消费人数
   */
  private Integer consumePeopleNum;
  /**
   * 交易笔数
   */
  private Integer transNum;
  /**
   * 人均消费
   */
  private String avgPeopleConsumeMoney;
  /**
   * 每笔平均交易额
   */
  private String avgTransMoney;
  /**
   * 消费总金额(按业务类型汇总)
   */
  private String consumeMoneyCollect;
  /**
   * 消费人数(按业务类型汇总)
   */
  private Integer consumePeopleNumCollect;
  /**
   * 交易笔数(按业务类型汇总)
   */
  private Integer transNumCollect;
  /**
   * 人均消费(按业务类型汇总)
   */
  private String avgPeopleConsumeMoneyCollect;
  /**
   * 每笔平均交易额(按业务类型汇总)
   */
  private String avgTransMoneyCollect;
  /**
   * 消费商户数
   */
  private Integer allMerNum;
  /**
   * 自营消费商户数
   */
  private Integer selfMerNum;
  /**
   * 非自营消费商户数
   */
  private Integer thirdMerNum;

  /**
   * 类型标注，1明细 3总的汇总数据 4 最顶部的数据
   */
  private String attribute;

  /**
   * 付费类型
   */
  private String merCooperationMode;

  private String bhConsumeMoneyCollect;

  private String bhConsumeMoney;

  private String dqConsumeMoneyCollect;

  private String dqConsumeMoney;

  private String csConsumeMoneyCollect;

  private String csConsumeMoney;

  /**
   * 累计充值额度
   */
  private String chargeBalance;

  /**
   * 在相应时间内访问app的人数
   */
  private String visitAppNum;

  /**
   * 在相应时间内每天访问app的人数
   */
  private String avgVisitAppNum;
}
