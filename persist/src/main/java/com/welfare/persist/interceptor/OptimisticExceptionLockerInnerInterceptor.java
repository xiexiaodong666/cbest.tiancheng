package com.welfare.persist.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.OptimisticLockingFailureException;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        if (SqlCommandType.UPDATE != ms.getSqlCommandType()) {
            return;
        }
        if (parameter instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameter;
            doOptimisticLocker(map, ms,executor);
        }
    }

    protected void doOptimisticLocker(Map<String, Object> map, MappedStatement ms,Executor executor) {
        //updateById(et), update(et, wrapper);
        Object et = map.getOrDefault(Constants.ENTITY, null);
        String msId = ms.getId();
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
                String keyProperty = tableInfo.getKeyProperty();
                Field field = ReflectUtil.getField(et.getClass(), keyProperty);
                field.setAccessible(true);
                Object dataId = field.get(et);
                String selectByIdMsId = msId.substring(0, msId.lastIndexOf(StringPool.DOT)) + StringPool.DOT + "selectById";
                List<Object> query = executor.query(ms.getConfiguration().getMappedStatement(selectByIdMsId), dataId, new RowBounds(), Executor.NO_RESULT_HANDLER);
                if(Objects.isNull(query)){
                    throw new BizException(ExceptionCode.DATA_NOT_EXIST);
                }else{
                    Object currentDataInDb = query.get(0);
                    Object o = versionField.get(currentDataInDb);
                    if(!Objects.equals(o,originalVersionVal)){
                        throw new OptimisticLockingFailureException("数据已被其他线程修改");
                    }
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
            } catch (IllegalAccessException | SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }
}
