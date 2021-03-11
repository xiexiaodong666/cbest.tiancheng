package com.welfare.service.remote.entity.response;

import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 3:00 PM
 */
@Data
public class WoLifeBasicResponse<T> {

  /**
   * 0表示请求失败,1=请求成功
   */
  private String responseCode;

  /**
   * 请求成功返回“success”，失败则返回 失败详情信息
   */
  private String responseMessage;

  /**
   * 返回对象
   */
  private T response;
}
