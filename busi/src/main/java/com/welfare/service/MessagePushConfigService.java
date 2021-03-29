package com.welfare.service;


import com.welfare.persist.entity.MessagePushConfig;

/**
 * 服务接口
 *
 * @author kancy
 * @since 2021-03-19 11:01:29
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MessagePushConfigService {

  /**
   * 初始化模板
   * @param merCode
   * @return
   */
  boolean init(String merCode);

}
