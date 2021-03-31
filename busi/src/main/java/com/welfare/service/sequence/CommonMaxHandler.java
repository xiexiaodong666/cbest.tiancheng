package com.welfare.service.sequence;

import com.welfare.common.exception.BizException;
import com.welfare.persist.entity.Sequence;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
public class CommonMaxHandler implements MaxSequenceExceedHandler{


    @Override
    public Sequence handle(Sequence sequence) {
        if(sequence.getSequenceNo() >sequence.getMaxSequence()){
            throw new BizException("自增序列达到最大值");
        }
        return sequence;
    }
}
