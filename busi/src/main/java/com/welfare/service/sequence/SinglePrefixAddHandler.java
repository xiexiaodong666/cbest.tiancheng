package com.welfare.service.sequence;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Sequence;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
public class SinglePrefixAddHandler implements MaxSequenceExceedHandler{

    public static final char Z = 'Z';

    @Override
    public Sequence handle(Sequence sequence) {
        String prefix = sequence.getPrefix();
        char[] chars = prefix.toCharArray();
        char c = (char) (chars[0] + 1);
        if(c > Z){
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"序列号前缀达到最大",null);
        }
        sequence.setPrefix(String.valueOf(c));
        sequence.setSequenceNo(sequence.getMinSequence());
        return sequence;
    }
}
