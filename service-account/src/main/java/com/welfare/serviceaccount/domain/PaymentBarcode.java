package com.welfare.serviceaccount.domain;

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
        paymentBarcode.setBarcode(generateBarcode(accountCode,secretKey));
        return paymentBarcode;
    }

    /**
     * 生成条码
      * @param accountCode
     * @return
     */
    private static String generateBarcode(Long accountCode, Long secretKey){
        /**
         *  barcode = 69 + [account + secretKey] 9位 + [rand1]3位 + [rand2]3位 + [(account + secretKey) % rand1]3位
         */
        Long secretAccount = accountCode + secretKey;

        // 保证rand1/rand2 < 1,防止计算后超过account的位数
        long rand1 = MathUtil.randomLong(100, 499);
        long rand2 = MathUtil.randomLong(500, 999);
        long mod = secretAccount % rand1;
        long secretResult = secretAccount / rand1 * rand2;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%09d",secretResult))
                .append(rand1)
                .append(rand2)
                .append(String.format("%03d",mod));
        String barcode = "69" + StringUtil.reverse(stringBuilder.toString());
        return barcode;
    }

    private static Long calculateAccount(String barcode,Long secretKey){
        /**
         * 1. 20位去掉前面固定的69
         * 2. reverse
         * 3. code = [Account + secretKey]9位 + [rand1]3位 + [rand2]3位 + [mod]3位
         * 4. 根据code求出Account
         */
        String barcodeWithoutPrefix = barcode.substring(2, 20);
        String reversed = StringUtil.reverse(barcodeWithoutPrefix);
        long secretResult = Long.parseLong(reversed.substring(0, 9));
        long rand1 = Long.parseLong(reversed.substring(9, 12));
        long rand2 = Long.parseLong(reversed.substring(12, 15));
        long mod = Long.parseLong(reversed.substring(15, 18));
        long accountCode = secretResult / rand2 * rand1 + mod - secretKey;
        return accountCode;
    }
}
