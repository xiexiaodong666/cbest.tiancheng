package com.welfare.service.remote.entity;

import lombok.Data;

import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:12 PM
 */
@Data
public class RoleConsumptionBindingsReq {

  private List<String> consumeTypes;

  private String storeCode;
}
