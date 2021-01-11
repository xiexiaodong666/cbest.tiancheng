package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@ApiModel("刷卡支付请求")
@Data
public class CardPaymentRequest extends AbstractPaymentRequest{
    @ApiModelProperty(value = "卡内信息",required = true)
    private String cardInsideInfo;
    @ApiModelProperty(value = "卡号")
    private String cardNo;
    @ApiModelProperty(value = "卡条码")
    private String cardBarcode;
}
