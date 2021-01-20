package com.welfare.service.remote.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 17:17
 */
@Data
public class StoreBinding implements Serializable {
  private List<String> consumeTypes;
  private String storeCode;
}
