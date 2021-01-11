package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/6/2021
 */
@ApiModel("支付请求")
@Data
public class OnlinePaymentRequest extends AbstractPaymentRequest {

    @ApiModelProperty("账户编码")
    private String accountCode;

}
