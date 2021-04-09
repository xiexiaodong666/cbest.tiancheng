package com.welfare.service.dto.messagepushconfig;

import com.google.common.collect.Lists;
import com.welfare.persist.entity.MessagePushConfigContact;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 4:56 下午
 */
@Data
public class WarningSettingSaveReq {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 商户编码
   */
  private String merchantCode;

  /**
   * 姓名
   */
  private String name;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 发送时间列表，格式”HH:mm”
   */
  private List<String> sendTimes;

  /**
   * 发送模板
   */
  private String template;

  /**
   * 状态，1.有效 0.无效
   */
  private Integer status;

  public static WarningSettingSaveReq of(MessagePushConfigContact contact, String content) {
    if (Objects.nonNull(contact)) {
      WarningSettingSaveReq req = new WarningSettingSaveReq();
      req.setId(contact.getId());
      req.setMerchantCode(contact.getMerCode());
      req.setName(contact.getContactPerson());
      req.setPhone(contact.getContact());
      req.setSendTimes(Lists.newArrayList(contact.getPushTime().split(";")));
      req.setTemplate(content);
      req.setStatus((contact.getDeleted() == null || !contact.getDeleted()) ? 1 : 0);
      return req;
    }
    return null;
  }
}
