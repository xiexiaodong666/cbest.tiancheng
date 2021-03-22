package com.welfare.servicemerchant.service.sync.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.MessagePushConfig;
import com.welfare.persist.entity.MessagePushConfigContact;
import com.welfare.service.dto.MessagePushConfigDao;
import com.welfare.service.dto.messagepushconfig.WarningSettingSaveReq;
import com.welfare.service.remote.CbestDmallFeign;
import com.welfare.service.remote.entity.pos.DmallResponse;
import com.welfare.service.sync.event.MessagePushConfigEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 5:13 下午
 */
@Component
@Slf4j
public class MessagePushConfigHandler {

  private final static String DEFAULT_TEMPLATE_CONTENT = "甜橙生活：今日挂起订单{todayOrderCount}笔，交易额{todayOrderAmount}元，累计挂起订单{totalOrderCount}笔，交易额{totalAccountCount}元，请登录甜橙生活查看并处理，以免影响正常交易。";

  @Autowired
  PersistentBus persistentBus;
  @Autowired(required = false)
  private CbestDmallFeign cbestDmallFeign;
  @Autowired
  private MessagePushConfigDao messagePushConfigDao;

  @PostConstruct
  private void register() {
    try {
      persistentBus.register(this);
    } catch (PersistentBus.EventBusException e) {
      log.error(e.getMessage(), e);
    }
  }

  @AllowConcurrentEvents
  @Subscribe
  public void onMessagePushConfigChange(MessagePushConfigEvt evt) {
    log.info("离线消息配置同步请求数据开始:{}", JSON.toJSONString(evt.getContact()));
    MessagePushConfigContact contact = evt.getContact();
    BizAssert.notNull(contact, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户消息联系人配置为空");
    MessagePushConfig config = messagePushConfigDao.getByMerCode(contact.getMerCode());
    String templateContent = DEFAULT_TEMPLATE_CONTENT;
    if (config == null) {
      log.warn("商户[{}]没有离线消息配置！", contact.getMerCode());
    } else {
      templateContent = config.getTemplateContent();
    }
    WarningSettingSaveReq req = WarningSettingSaveReq.of(contact, templateContent);
    log.info("离线消息配置同步请求数据 请求:{}", JSON.toJSONString(req));
    DmallResponse<Object> resp = cbestDmallFeign.saveWarningSetting(req);
    log.info("离线消息配置同步请求数据完成 响应:{} ,请求:{}", JSON.toJSONString(resp), JSON.toJSONString(req));
    if (!(CbestDmallFeign.SUCCESS_CODE).equals(resp.getCode())) {
      throw new BizException(String.format("离线消息配置同步请求数据失败 请求:%s msg:%s", JSON.toJSONString(req), resp.getMsg()));
    }
  }
}
