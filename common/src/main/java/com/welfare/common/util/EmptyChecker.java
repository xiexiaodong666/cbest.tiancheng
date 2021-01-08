package com.welfare.common.util;

import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Created by hao.yin on 2019/11/19.
 */
public class EmptyChecker {
    public static boolean isEmpty(Object o){
        if(o instanceof Collection){
           return  ((Collection) o).size()==0||o==null;
        }else if(o instanceof String){
           return StringUtils.isEmpty(o);
        }else{
            return o==null;
        }
    }

    public static boolean notEmpty(Object o){
        return !isEmpty(o);
    }
}
