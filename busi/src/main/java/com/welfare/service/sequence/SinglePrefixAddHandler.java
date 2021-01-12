package com.welfare.service.sequence;

import com.welfare.persist.entity.Sequence;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
public class SampleHandler implements MaxSequenceExceedHandler{
    @Override
    public Sequence handle(Sequence sequence) {
        /**
         * 样例，不做任何处理，直接返回
         */
        return sequence;
    }
}
