package com.welfare.common.exception;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/19/2021
 */
public class BizAssert {
    public static void isTrue(boolean expression,ExceptionCode exceptionCode,String exceptionMsg){
        if(!expression){
            throw new BizException(exceptionCode,exceptionMsg,null);
        }
    }

    public static void isTrue(boolean expression,ExceptionCode exceptionCode){
        if(!expression){
            throw new BizException(exceptionCode,exceptionCode.getMsg(),null);
        }
    }

    public static void isTrue(boolean expression){
        if(!expression){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,ExceptionCode.ILLEGALITY_ARGURMENTS.getMsg(), null);
        }
    }

    public static void notNull(Object obj){
        if(Objects.isNull(obj)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,ExceptionCode.ILLEGALITY_ARGURMENTS.getMsg(), null);
        }
    }

    public static void notNull(Object obj,ExceptionCode exceptionCode,String exceptionMsg){
        if(Objects.isNull(obj)){
            throw new BizException(exceptionCode,exceptionMsg, null);
        }
    }

    public static void notNull(Object obj,ExceptionCode exceptionCode){
        if(Objects.isNull(obj)){
            throw new BizException(exceptionCode,exceptionCode.getMsg(), null);
        }
    }

    public static void notEmpty(Collection<?> collection, ExceptionCode exceptionCode){
        if(CollectionUtils.isEmpty(collection)){
            throw new BizException(exceptionCode);
        }
    }

    public static void notEmpty(Collection<?> collection, ExceptionCode exceptionCode, String msg){
        if(CollectionUtils.isEmpty(collection)){
            throw new BizException(exceptionCode,msg);
        }
    }

    public static void notEmpty(Object[] objs, ExceptionCode exceptionCode){
        if(objs == null || objs.length == 0){
            throw new BizException(exceptionCode);
        }
    }

    public static void notEmpty(Object[] objs, ExceptionCode exceptionCode, String msg){
        if(objs == null || objs.length == 0){
            throw new BizException(exceptionCode);
        }
    }

    public static void notBlank(String str){
        if(StringUtils.isBlank(str)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,ExceptionCode.ILLEGALITY_ARGURMENTS.getMsg(), null);
        }
    }

    public static void notBlank(String str, ExceptionCode exceptionCode,String exceptionMsg){
        if(StringUtils.isBlank(str)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,ExceptionCode.ILLEGALITY_ARGURMENTS.getMsg(), null);
        }
    }
}
