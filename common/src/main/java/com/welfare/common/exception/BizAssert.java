package com.welfare.common.exception;

import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/19/2021
 */
public class BizAssert {
    public static void isTrue(boolean b,ExceptionCode exceptionCode,String exceptionMsg){
        if(!b){
            throw new BizException(exceptionCode,exceptionMsg,null);
        }
    }

    public static void notNull(Object obj){
        if(Objects.isNull(obj)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,ExceptionCode.ILLEGALITY_ARGURMENTS.getMsg(), null);
        }
    }
}
