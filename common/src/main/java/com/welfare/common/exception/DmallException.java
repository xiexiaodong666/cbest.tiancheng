package com.welfare.common.exception;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/23 7:00 下午
 */
public class DmallException extends RuntimeException{

  private Integer code;
  private String msg;

  public DmallException(Integer code, String msg, Throwable cause) {
    super(msg, cause);
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
