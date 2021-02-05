package com.welfare.common.annotation;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/5/2021
 */

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ConditionalOnHavingPropertyHandler.class)
public @interface ConditionalOnHavingProperty {
    /**
     * 填入a.b.c  而不是${a.b.c}
     * @return
     */
    String value() default "";
}
