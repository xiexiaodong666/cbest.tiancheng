package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/8 10:55 上午
 */
@Data
public class EmployeeSettleBuildDTO {

  @ApiModelProperty("员工Id")
  private Long accountCode;

  @ApiModelProperty("员工姓名")
  private String accountName;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("组织机构")
  private String departmentName;

  @ApiModelProperty("授信额度")
  private String quota;

  @ApiModelProperty("消费总额")
  private BigDecimal totalConsumerAmount;

  @ApiModelProperty("结算总额")
  private BigDecimal totalSettleAmount;

  @ApiModelProperty("消费笔数")
  private Integer orderNum;

  @ApiModelProperty("最早消费时间")
  private Date minTransTime;

  @ApiModelProperty("最晚消费时间")
  private Date maxTransTime;
}
