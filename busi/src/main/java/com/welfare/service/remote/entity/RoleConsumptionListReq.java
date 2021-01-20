package com.welfare.service.remote.entity;

import lombok.Data;

import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:07 PM
 */
@Data
public class RoleConsumptionListReq {


  private String merchantCode;
  private Boolean enabled;
  private List<RoleConsumptionBindingsReq> bindings;

}
