package com.welfare.service.remote.entity.pos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:38 上午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmallResponse <T> {

  private Integer code;

  /**
   * 成功
   */
  private String msg;

  private T data;
}
