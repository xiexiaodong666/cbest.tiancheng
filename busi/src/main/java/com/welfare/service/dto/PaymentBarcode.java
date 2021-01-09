package com.welfare.service.dto;

import com.welfare.common.util.BarcodeUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
public class PaymentBarcode {
    private String barcode;
    private Date generatedDate;
    public static PaymentBarcode of(Long accountCode,Long secretKey){
        PaymentBarcode paymentBarcode = new PaymentBarcode();
        paymentBarcode.setBarcode(BarcodeUtil.generateBarcode(accountCode,secretKey));
        paymentBarcode.setGeneratedDate(Calendar.getInstance().getTime());
        return paymentBarcode;
    }
}
