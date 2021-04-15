package com.welfare.service.dto.paymentChannel;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:36 下午
 */
@Data
public class PayChannelConfigSimpleReq extends PageReq {

  @ApiModelProperty("商户名称")
  private String merchantName;
}
