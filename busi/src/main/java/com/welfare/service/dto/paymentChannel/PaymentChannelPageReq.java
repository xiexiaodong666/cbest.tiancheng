package com.welfare.service.dto.paymentChannel;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/9 3:01 下午
 */
@Data
public class PaymentChannelPageReq extends PageReq {
  @ApiModelProperty("商户名称")
  private String merName;
}
