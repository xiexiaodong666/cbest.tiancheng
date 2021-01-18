package com.welfare.servicemerchant.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/18 11:33
 */
@Data
public class UpdateStatusReq implements Serializable {
  private String id;
  private Integer accountStatus;
  private Integer consumeSceneStatus;
}
