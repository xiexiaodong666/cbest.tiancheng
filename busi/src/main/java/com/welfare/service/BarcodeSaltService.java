package com.welfare.service;

import com.welfare.persist.entity.BarcodeSalt;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
public interface BarcodeSaltService {
    /**
     * 查询barcodeSalt
     * @param fromValidPeriodNumeric 从哪一个period开始拉起
     * @return
     */
    List<BarcodeSalt> query(Long fromValidPeriodNumeric);

    /**
     * 生成指定有效期的加盐参数
     * @param targetValidPeriod
     * @return 生成的盐值，或者目标日期已经存在的盐值
     */
    BarcodeSalt generateSalt(Long targetValidPeriod);

    /**
     * 批量生成缺失的period
     */
    void batchGenerate();
}
