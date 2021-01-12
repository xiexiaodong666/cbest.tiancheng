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
    String nextFullNo(String sequenceType, String prefix, Long startId);

    /**
     * 生成下一个序列号
     * @param sequenceType
     * @param prefix
     * @param startId
     * @return
     */

    Long nextNo(String sequenceType, String prefix, Long startId);
}
