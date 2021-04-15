package com.welfare.service.dto.payment;

import com.welfare.common.util.BarcodeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Data
@ApiModel("支付条码")
public class PaymentBarcode implements Serializable {
    @ApiModelProperty("条码值")
    private String barcode;
    @ApiModelProperty("条码生成日期")
    private Date generatedDate;
    public static PaymentBarcode of(Long accountCode, Long secretKey, String paymentChannel, Date generatedDate){
        PaymentBarcode paymentBarcode = new PaymentBarcode();
        paymentBarcode.setBarcode(BarcodeUtil.generateBarcode(accountCode,secretKey, paymentChannel));
        paymentBarcode.setGeneratedDate(generatedDate);
        return paymentBarcode;
    }
}
