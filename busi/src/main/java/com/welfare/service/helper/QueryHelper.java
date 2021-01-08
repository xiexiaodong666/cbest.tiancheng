package com.welfare.service.helper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.welfare.common.annotation.Query;
import com.welfare.common.util.EmptyChecker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * @author hao.yin
 */
@Slf4j
public class QueryHelper {
  @SuppressWarnings("unchecked")
  public static <T>QueryWrapper getWrapper(T query) {
    QueryWrapper<T> wrapper = new QueryWrapper<T>();
    if (query == null) {
      return wrapper;
    }
    try {
      List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
      for (Field field : fields) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Query q = field.getAnnotation(Query.class);
        if (q != null) {
          String propName = q.propName();
          String blurry = q.blurry();
          String attributeName = isBlank(propName) ? field.getName() : propName;
          attributeName=CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, attributeName);
          Object val = field.get(query);
          if (EmptyChecker.isEmpty(val)) {
            continue;
          }
          if (EmptyChecker.notEmpty(blurry)) {
            String [] blurrys = blurry.split(",");
            wrapper.and(wr -> {
                      for (int k=0;k<blurrys.length-1 ; k++) {
                        if(k==0){
                          wrapper.like(blurrys[k], val.toString());
                        }
                        wrapper.or().like(blurrys[k], val.toString());
                      }
                    }

            );
            continue;
          }
          switch (q.type()) {
            case EQUAL:
              wrapper.eq(attributeName,val);
              break;
            case GREATER_THAN:
              wrapper.ge(attributeName,val);
              break;
            case LESS_THAN:
              wrapper.le(attributeName,val);
              break;
            case LESS_THAN_NQ:
              wrapper.lt(attributeName,val);
              break;
            case GREATER_THAN_NQ:
              wrapper.gt(attributeName,val);
              break;
            case INNER_LIKE:
              wrapper.like(attributeName,"%" + val.toString() );
              break;
            case LEFT_LIKE:
              wrapper.like(attributeName,"%" + val.toString() );
              break;
            case RIGHT_LIKE:
              wrapper.like(attributeName, val.toString() + "%");

            case IN:
              wrapper.in(attributeName, (Collection<Long>) val);
              break;
            default:
              break;
          }
        }
        field.setAccessible(accessible);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return wrapper;
  }


  private static boolean isBlank(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private static List<Field> getAllFields(Class clazz, List<Field> fields) {
    if (clazz != null) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      getAllFields(clazz.getSuperclass(), fields);
    }
    return fields;
  }
}
