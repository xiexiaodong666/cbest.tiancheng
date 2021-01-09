package com.welfare.serviceaccount.domain;

import com.welfare.common.util.BarcodeUtil;
import io.swagger.annotations.ApiModel;
import jodd.util.MathUtil;
import jodd.util.StringUtil;
import lombok.Data;

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

    public static PaymentBarcode of(Long accountCode,Long secretKey){
        PaymentBarcode paymentBarcode = new PaymentBarcode();
        paymentBarcode.setBarcode(BarcodeUtil.generateBarcode(accountCode,secretKey));
        return paymentBarcode;
    }
}
