package com.welfare.persist.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Calendar;

/**
 * Description: insert和update前，赋值通用字段
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Slf4j
public class CommonFieldObjectHandler implements MetaObjectHandler {
    private static final String CREATOR = "createUser";
    private static final String UPDATER = "updateUser";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String VERSION = "version";
    private static final String DELETED = "deleted";
    @Override
    public void insertFill(MetaObject metaObject) {
      log.debug("ready to fill on insert operation ....");
      //todo 获取用户信息
      this.setFieldValByName(CREATOR,"todo",metaObject);
      this.setFieldValByName(CREATE_TIME, Calendar.getInstance().getTime(),metaObject);
      this.setFieldValByName(VERSION,0,metaObject);
      this.setFieldValByName(DELETED,false,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("ready to fill on update operation ....");
        //todo 获取用户信息
        this.setFieldValByName(UPDATER,"todo",metaObject);
        this.setFieldValByName(UPDATE_TIME, Calendar.getInstance().getTime(),metaObject);
    }
}
