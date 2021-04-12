package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 9:53 上午
 */
@Data
public class NhcUserInfoDTO {

  @ApiModelProperty(value = "用户名称")
  private String userName;

  @ApiModelProperty("用户编码")
  private String userCode;

  @ApiModelProperty(value = "商户CODE")
  private String merCode;

  @ApiModelProperty(value = "商户名称")
  private String merName;

  @ApiModelProperty(value = "用户手机号码")
  private String phone;

  @ApiModelProperty(value = "积分")
  private BigDecimal mallPoint;
}
