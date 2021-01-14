package com.welfare.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 10:17
 */
@Data
public class ConsumeTypeJson implements Serializable {
  private Boolean o2o;
  private Boolean onlineMall;
  private Boolean shopShopping;
}
