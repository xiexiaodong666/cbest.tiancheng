package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@ApiModel("扫码支付请求")
@Data
public class BarcodePaymentRequest extends AbstractPaymentRequest{
    @ApiModelProperty("条码")
    private String barcode;
    @ApiModelProperty("扫描日期，yyyy-MM-ddTHH:mm:ss+08:00,例:2021-01-01T12:00:00+08:00")
    private Date scanDate;
}
