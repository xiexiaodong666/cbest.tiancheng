package com.welfare.service.remote.entity.pos;

import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:38 上午
 */
@Data
public class DmallResponse <T> {

  /**
   * 成功：0000
   */
  private String code;

  /**
   * 成功
   */
  private String msg;

  private T data;
}
