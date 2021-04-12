package com.welfare.service.dto.nhc;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:14 上午
 */
@Data
public class NhcAccountBillDetailDTO {

  @ApiModelProperty("交易流水号")
  private String transNo;

  @ApiModelProperty("交易时间 yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date transTime;

  @ApiModelProperty("出入账")
  private String transTypeName;

  @ApiModelProperty("账户类型名称")
  private String merAccountTypeName;

  @ApiModelProperty("账户类型名称")
  private String merAccountTypeCode;

  @ApiModelProperty("交易总金额")
  private BigDecimal transAmount;

  @ApiModelProperty("事件")
  private String event;
}
