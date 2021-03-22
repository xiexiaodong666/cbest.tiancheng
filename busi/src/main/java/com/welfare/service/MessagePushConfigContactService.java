package com.welfare.service;


import com.welfare.service.dto.messagepushconfig.MessagConfigContactAddReq;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactEditReq;
import com.welfare.service.dto.messagepushconfig.MessagPushConfigContactDTO;

import java.util.List;

/**
 * 服务接口
 *
 * @author kancy
 * @since 2021-03-19 11:01:30
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MessagePushConfigContactService {

  /**
   * 新增短信配置
   * @param req
   * @return
   */
  boolean add(MessagConfigContactAddReq req);

  /**
   * 编辑短信配置
   * @param req
   * @return
   */
  boolean edit(MessagConfigContactEditReq req);

  /**
   * 删除短信配置
   * @param id
   * @return
   */
  boolean delete(String id);

  /**
   * 更加联系人信息查询配置
   * @param contact
   * @return
   */
  List<MessagPushConfigContactDTO> listByContact(String contact);

  /**
   * 根据id查询配置
   * @param id
   * @return
   */
  MessagPushConfigContactDTO findOneById(String id);
}
