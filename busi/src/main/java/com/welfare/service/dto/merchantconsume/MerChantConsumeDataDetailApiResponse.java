package com.welfare.service.dto.merchantconsume;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 11:33 AM
 */
@Data
public class MerChantConsumeDataDetailApiResponse {
  @ApiModelProperty("虚拟id")
  private Long id;

  @ApiModelProperty("消费方式")
  private String consumeType;

  @ApiModelProperty("消费总金额")
  private Double consumeMoney;

  @ApiModelProperty("百货消费总金额")
  private String bhConsumeMoney;

  @ApiModelProperty("超市消费总金额")
  private String dqConsumeMoney;

  @ApiModelProperty("电器消费总金额")
  private String csConsumeMoney;

  @ApiModelProperty("消费人数")
  private Integer consumePeopleNum;

  @ApiModelProperty("交易笔数")
  private Integer transNum;

  @ApiModelProperty("人均消费")
  private Double avgPeopleConsumeMoney;

  @ApiModelProperty("每笔平均交易额")
  private Double avgTransMoney;
}
