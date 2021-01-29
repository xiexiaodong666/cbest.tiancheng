package com.welfare.common.util;

import jodd.util.MathUtil;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/9/2021
 */
@Slf4j
public class BarcodeUtil {
    private BarcodeUtil(){

    }
    private static final Long MIN_SALT_VALUE = 10000000L;
    private static final Long MAX_SALT_VALUE = 89999999L;
    /**
     * 生成条码
     * @param accountCode
     * @return
     */
    public static String generateBarcode(Long accountCode, Long secretKey){
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
        stringBuilder.append(String.format("%010d",secretResult))
                .append(rand1)
                .append(rand2)
                .append(String.format("%03d",mod));
        String barcode = "69" + jodd.util.StringUtil.reverse(stringBuilder.toString());
        return barcode;
    }

    /**
     * 根据条码和secretKey计算账户code
     * @param barcode
     * @param secretKey
     * @return
     */
    public static Long calculateAccount(String barcode,Long secretKey){
        /**
         * 1. 21位去掉前面固定的69
         * 2. reverse
         * 3. code = [Account + secretKey]10位 + [rand1]3位 + [rand2]3位 + [mod]3位
         * 4. 根据code求出Account
         */
        String barcodeWithoutPrefix = barcode.substring(2, 21);
        String reversed = StringUtil.reverse(barcodeWithoutPrefix);
        long secretResult = Long.parseLong(reversed.substring(0, 10));
        long rand1 = Long.parseLong(reversed.substring(10, 13));
        long rand2 = Long.parseLong(reversed.substring(13, 16));
        long mod = Long.parseLong(reversed.substring(16, 19));
        long accountCode = secretResult / rand2 * rand1 + mod - secretKey;
        return accountCode;
    }

    /**
     * 生成一个10000000 - 89999999的saltValue
     * @return
     */
    public static Long nextRandSalt(){
        return MathUtil.randomLong(MIN_SALT_VALUE,MAX_SALT_VALUE);
    }

    /**
     * 将当前时间转为Period返回
     * @return
     * @param date
     */
    public static Long dateAsPeriod(Date date){
        if(date == null){
            log.warn("no date, use current.");
            date = Calendar.getInstance().getTime();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dayStr = dateFormat.format(date);
        return Long.parseLong(dayStr);
    }

    /**
     * 将period转为时间
     * @param period
     * @return
     */
    @SneakyThrows
    public static Date parsePeriodToDate(String period){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.parse(period);
    }
}
