package com.welfare.service;

import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.dto.payment.PaymentBarcode;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
public interface BarcodeService {
    /**
     * 查询barcodeSalt
     * @param fromValidPeriodNumeric 从哪一个period开始拉起
     * @return
     */
    List<BarcodeSalt> querySalt(Long fromValidPeriodNumeric);

    /**
     * 生成指定有效期的加盐参数
     * @param targetValidPeriod
     * @return 生成的盐值，或者目标日期已经存在的盐值
     */
    BarcodeSalt generateSalt(Long targetValidPeriod);

    /**
     * 批量生成缺失的period
     */
    void batchGenerateSalt();

    /**
     * 获取当前周期的saltValue
     * @return
     * @param scanDate
     */
    BarcodeSalt queryPeriodSaltValue(Date scanDate);

    /**
     * 根据period查询单条barcodeSalt
     * @param period
     * @return
     */
    BarcodeSalt querySaltByPeriodNumeric(Long period);

    /**
     * 获取一个支付条码
     * @param accountCode
     * @param paymentChannel
     * @return
     */
    PaymentBarcode getBarcode(Long accountCode, String paymentChannel);

    /**
     * 从barcode解析账户号
     * @param barcode
     * @param scanDate
     * @param isOffline
     * @return
     */
    Long parseAccountFromBarcode(String barcode, Date scanDate, boolean isOffline);
}
