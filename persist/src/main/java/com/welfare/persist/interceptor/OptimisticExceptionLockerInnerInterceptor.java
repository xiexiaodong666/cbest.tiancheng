package com.welfare.persist.interceptor;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/21/2021
 */
public class OptimisticExceptionLockerInnerInterceptor extends OptimisticLockerInnerInterceptor {
    private static final String PARAM_UPDATE_METHOD_NAME = "update";

    @Override
    protected void doOptimisticLocker(Map<String, Object> map, String msId) {
        //updateById(et), update(et, wrapper);
        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (et != null) {
            // entity
            String methodName = msId.substring(msId.lastIndexOf(StringPool.DOT) + 1);
            TableInfo tableInfo = TableInfoHelper.getTableInfo(et.getClass());
            if (tableInfo == null || !tableInfo.isWithVersion()) {
                return;
            }
            try {
                TableFieldInfo fieldInfo = tableInfo.getVersionFieldInfo();
                Field versionField = fieldInfo.getField();
                // 旧的 version 值
                Object originalVersionVal = versionField.get(et);
                if (originalVersionVal == null) {
                    return;
                }
                String versionColumn = fieldInfo.getColumn();
                // 新的 version 值
                Object updatedVersionVal = this.getUpdatedVersionVal(fieldInfo.getPropertyType(), originalVersionVal);
                if (PARAM_UPDATE_METHOD_NAME.equals(methodName)) {
                    AbstractWrapper<?, ?, ?> aw = (AbstractWrapper<?, ?, ?>) map.getOrDefault(Constants.WRAPPER, null);
                    if (aw == null) {
                        UpdateWrapper<?> uw = new UpdateWrapper<>();
                        uw.eq(versionColumn, originalVersionVal);
                        map.put(Constants.WRAPPER, uw);
                    } else {
                        aw.apply(versionColumn + " = {0}", originalVersionVal);
                    }
                } else {
                    map.put(Constants.MP_OPTLOCK_VERSION_ORIGINAL, originalVersionVal);
                }
                versionField.set(et, updatedVersionVal);
            } catch (IllegalAccessException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }
}
