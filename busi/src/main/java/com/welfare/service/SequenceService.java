package com.welfare.service;

import com.welfare.service.dto.BatchSequence;

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
    Long nextNo(String sequenceType);

    /**
     * 生成下一个序列号，拼接上前缀
     * @param sequenceType
     * @return
     */
    String nextFullNo(String sequenceType);

    /**
     * 生成下一个序列号，拼接上前缀
     * @param sequenceType
     * @return
     */
    String nextFullNo(String sequenceType, String prefix, Long startId, int num);

    /**
     * 生成下一个序列号
     * @param sequenceType
     * @param prefix
     * @param startId
     * @return
     */

    Long nextNo(String sequenceType, String prefix, Long startId, int num);

    /**
     *  批量生成序列号
     * @param sequenceType
     * @param total
     * @return
     */
    BatchSequence batchGenerate(String sequenceType, int total);
}
