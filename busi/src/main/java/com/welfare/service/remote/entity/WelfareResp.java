package com.welfare.service.remote.entity;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/10 1:37 下午
 */
@Data
public class WelfareResp {

  /**
   * 1-成功 -1失败
   */
  private int code;

  private boolean success;

  private String msg;

  private T data;
}
