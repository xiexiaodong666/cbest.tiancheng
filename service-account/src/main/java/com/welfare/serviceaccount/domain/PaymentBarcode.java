package com.welfare.serviceaccount.domain;

import io.swagger.annotations.ApiModel;
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

    public static PaymentBarcode of(String accountCode,String key,String cardNo){
        PaymentBarcode paymentBarcode = new PaymentBarcode();
        paymentBarcode.setBarcode(generateBarcode(accountCode,key,cardNo));
        return paymentBarcode;
    }

    /**
     * 修改生成条码算法 todo
      * @param accountCode
     * @param key
     * @param cardNo
     * @return
     */
    private static String generateBarcode(String accountCode,String key,String cardNo){
        return accountCode + key + cardNo;
    }
}
