package com.welfare.service;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
public interface SequenceService {
    /**
     * 生成下一个序列号
     * @param sequenceType
     * @return
     */
    Long next(String sequenceType);
}
