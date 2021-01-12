package com.welfare.service.remote.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:05 PM
 */
@Data
public class RoleConsumptionReq {

  private String actionType;
  private List<RoleConsumptionListReq> list;
  private String requestId;

  private Date timestamp;
}
