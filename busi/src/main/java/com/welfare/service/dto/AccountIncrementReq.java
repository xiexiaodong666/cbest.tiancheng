package com.welfare.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 15:11
 */
@Data
public class AccountIncrementReq implements Serializable {
  @NotNull(message="门店编码不能为空")
  private String storeCode;
  @NotNull(message="账户变更记录ID不能为空")
  private Long changeEventId;
  @NotNull(message="查询size不能为空")
  private Integer size;
}
