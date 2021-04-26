package com.welfare.service.impl;

import com.google.common.collect.Lists;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.common.util.StringUtil;
import com.welfare.persist.entity.MessagePushConfig;
import com.welfare.persist.entity.MessagePushConfigContact;
import com.welfare.service.MessagePushConfigContactService;
import com.welfare.service.dto.MessagePushConfigContactDao;
import com.welfare.service.dto.MessagePushConfigDao;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactAddReq;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactEditReq;
import com.welfare.service.dto.messagepushconfig.MessagPushConfigContactDTO;
import com.welfare.service.sync.event.MessagePushConfigEvt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 服务接口实现
 *
 * @author kancy
 * @since 2021-03-19 11:01:30
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MessagePushConfigContactServiceImpl implements MessagePushConfigContactService {

    private final MessagePushConfigContactDao messagePushConfigContactDao;
    private final MessagePushConfigDao messagePushConfigDao;
    private final ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(MessagConfigContactAddReq req) {
        // 查询主表信息
        String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
        MessagePushConfig config = messagePushConfigDao.getByMerCode(merCode);
        BizAssert.notNull(config, ExceptionCode.ILLEGALITY_ARGUMENTS, "商户短信配置不存在");
        req.getPushTimes().forEach(s -> {
            BizAssert.isTrue(StringUtil.validationHHmm(s),
                    ExceptionCode.ILLEGALITY_ARGUMENTS, String.format("发送时间格式错误[%s]", s));
        });
        BizAssert.isTrue(messagePushConfigContactDao.getByMerCodeAndContact(merCode, req.getContact()) == null,
                ExceptionCode.ILLEGALITY_ARGUMENTS, String.format("配置已存在[%s]", req.getContact()));
        MessagePushConfigContact contact = new MessagePushConfigContact();
        contact.setMessagePushConfigId(config.getId());
        contact.setConfigCode(contact.getConfigCode());
        contact.setMerCode(config.getMerCode());
        contact.setConfigName(config.getConfigName());
        contact.setPushTime(String.join(";", req.getPushTimes()));
        contact.setContactPerson(req.getContactPerson());
        contact.setContact(req.getContact());
        contact.setConfigCode(config.getConfigCode());
        contact.setCreateUser(MerchantUserHolder.getMerchantUser().getUserCode());
        boolean flag = messagePushConfigContactDao.save(contact);
        // 推送给智慧食堂
        if (flag) {
            applicationContext.publishEvent(MessagePushConfigEvt.builder().contact(contact).build());
        }
        return contact.getId() + "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String edit(MessagConfigContactEditReq req) {
        String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
        MessagePushConfig config = messagePushConfigDao.getByMerCode(merCode);
        BizAssert.notNull(config, ExceptionCode.ILLEGALITY_ARGUMENTS, "商户短信配置不存在");
        req.getPushTimes().forEach(s -> {
            BizAssert.isTrue(StringUtil.validationHHmm(s),
                    ExceptionCode.ILLEGALITY_ARGUMENTS, String.format("发送时间格式错误[%s]", s));
        });
        MessagePushConfigContact contact = messagePushConfigContactDao.getById(req.getId());
        BizAssert.notNull(contact, ExceptionCode.ILLEGALITY_ARGUMENTS, String.format("配置不已存在[%s]", req.getId()));
        contact.setContact(req.getContact());
        contact.setContactPerson(req.getContactPerson());
        contact.setPushTime(String.join(";", req.getPushTimes()));
        contact.setUpdateUser(MerchantUserHolder.getMerchantUser().getUserCode());
        boolean flag = messagePushConfigContactDao.updateById(contact);
        // 推送给智慧食堂
        if (flag) {
            applicationContext.publishEvent(MessagePushConfigEvt.builder().contact(contact).build());
        }
        return contact.getId() + "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        MessagePushConfigContact contact = messagePushConfigContactDao.getById(id);
        BizAssert.notNull(contact, ExceptionCode.ILLEGALITY_ARGUMENTS, String.format("配置不已存在[%s]", id));
        boolean flag = messagePushConfigContactDao.removeById(id);
        // 推送给智慧食堂
        if (flag) {
            contact.setDeleted(Boolean.TRUE);
            contact.setUpdateUser(MerchantUserHolder.getMerchantUser().getUserCode());
            applicationContext.publishEvent(MessagePushConfigEvt.builder().contact(contact).build());
        }
        return flag;
    }

    @Override
    public List<MessagPushConfigContactDTO> listByContact(String contact) {
        List<MessagPushConfigContactDTO> result = new ArrayList<>();
        String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
        List<MessagePushConfigContact> contacts =  messagePushConfigContactDao.listByMerCodeAndContact(merCode, contact);
        if (CollectionUtils.isNotEmpty(contacts)) {
            contacts.forEach(messagePushConfigContact -> {
                MessagPushConfigContactDTO dto = new MessagPushConfigContactDTO();
                dto.setId(messagePushConfigContact.getId() + "");
                dto.setMerCode(messagePushConfigContact.getMerCode());
                dto.setContact(messagePushConfigContact.getContact());
                dto.setContactPerson(messagePushConfigContact.getContactPerson());
                dto.setPushTimes(Lists.newArrayList(messagePushConfigContact.getPushTime().split(";")));
                result.add(dto);
            });
        }
        return result;
    }

    @Override
    public MessagPushConfigContactDTO findOneById(String id) {
        MessagePushConfigContact messagePushConfigContact = messagePushConfigContactDao.getById(id);
        if (Objects.nonNull(messagePushConfigContact)) {
            MessagPushConfigContactDTO dto = new MessagPushConfigContactDTO();
            dto.setId(messagePushConfigContact.getId() + "");
            dto.setMerCode(messagePushConfigContact.getMerCode());
            dto.setContact(messagePushConfigContact.getContact());
            dto.setContactPerson(messagePushConfigContact.getContactPerson());
            dto.setPushTimes(Lists.newArrayList(messagePushConfigContact.getPushTime().split(";")));
            return dto;
        }
        return null;
    }
}