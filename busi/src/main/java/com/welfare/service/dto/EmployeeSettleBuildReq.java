package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:10 下午
 */
@Data
@ApiModel("员工授信额度结算生成对象")
public class EmployeeSettleBuildReq {

  @ApiModelProperty("员工姓名")
  private String accountName;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("机构编码")
  private Set<String> departments;

  @ApiModelProperty("订单编号")
  private String orderId;

  @ApiModelProperty("消费流水号")
  private String transNo;

  @ApiModelProperty("消费类型 自营:self, 第三方:third")
  private String storeType;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty("消费起始时间")
  private Date transTimeStart;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty("消费截止时间")
  private Date transTimeEnd;

  @ApiModelProperty("员工支出方式 授信额度:surplus_quota, 溢缴款:")
  private String merAccountType;

  @ApiModelProperty("勾选的员工编码")
  @NotEmpty(message = "至少勾选一个员工！")
  private List<String> selectedAccountCodes;
}
