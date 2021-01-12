package com.welfare.service.sequence;

import com.welfare.persist.entity.Sequence;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
public interface MaxSequenceExceedHandler {

    /**
     * 当达到最大的序列后对序列号的操作
     * @param sequence
     * @return
     */
    Sequence handle(Sequence sequence);
}
