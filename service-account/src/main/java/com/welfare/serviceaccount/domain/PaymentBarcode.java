package com.welfare.serviceaccount.domain;

import io.swagger.annotations.ApiModel;
import jodd.util.MathUtil;
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
        paymentBarcode.setBarcode(generateBarcode(accountCode,secretKey));
        return paymentBarcode;
    }

    /**
     * 修改生成条码算法
      * @param accountCode
     * @return
     */
    private static String generateBarcode(Long accountCode,Long secretKey){
        Long x = accountCode + secretKey;
        long firstRand = MathUtil.randomLong(100, 999);
        long secondRand = MathUtil.randomLong(100, 999);
        long mod = x % firstRand;
        long l = x / firstRand * secondRand;
        return accountCode.toString();
    }
}
