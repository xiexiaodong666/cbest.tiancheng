package com.welfare.service.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.MessagePushConfig;
import com.welfare.persist.mapper.MessagePushConfigMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * (message_push_config)数据DAO
 *
 * @author kancy
 * @since 2021-03-19 11:01:29
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MessagePushConfigDao extends ServiceImpl<MessagePushConfigMapper, MessagePushConfig> {

  public MessagePushConfig getByMerCode(String merCode) {
    if (StringUtils.isNotBlank(merCode)) {
      QueryWrapper<MessagePushConfig> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(MessagePushConfig.MER_CODE, merCode);
      return getOne(queryWrapper);
    }
    return null;
  }
}