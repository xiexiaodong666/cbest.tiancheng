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
}
