package com.welfare.service.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.MessagePushConfigContact;
import com.welfare.persist.mapper.MessagePushConfigContactMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * (message_push_config_contact)数据DAO
 *
 * @author kancy
 * @since 2021-03-19 11:01:30
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MessagePushConfigContactDao extends ServiceImpl<MessagePushConfigContactMapper, MessagePushConfigContact> {

  public MessagePushConfigContact getByMerCodeAndContact(String merCode, String contact) {
    if (StringUtils.isNoneBlank(merCode) && StringUtils.isNoneBlank(contact)) {
      QueryWrapper<MessagePushConfigContact> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(MessagePushConfigContact.MER_CODE, merCode);
      queryWrapper.eq(MessagePushConfigContact.CONTACT, contact);
      return getOne(queryWrapper);
    }
    return null;
  }

  public List<MessagePushConfigContact> listByMerCodeAndContact(String merCode, String contact) {
    List<MessagePushConfigContact> list = new ArrayList<>();
    if (StringUtils.isNoneBlank(merCode)) {
      QueryWrapper<MessagePushConfigContact> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(MessagePushConfigContact.MER_CODE, merCode);
      if (StringUtils.isNoneBlank(contact)) {
        queryWrapper.eq(MessagePushConfigContact.CONTACT, contact);
      }
      return list(queryWrapper);
    }
    return list;
  }
}