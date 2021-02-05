package com.welfare.common.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/5/2021
 */
public class ConditionalOnHavingPropertyHandler implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnHavingProperty.class.getName());
        String propertyName = annotationAttributes.get("value").toString();
        String property = conditionContext.getEnvironment().getProperty(propertyName,"");
        return !StringUtils.isEmpty(property);
    }
}
