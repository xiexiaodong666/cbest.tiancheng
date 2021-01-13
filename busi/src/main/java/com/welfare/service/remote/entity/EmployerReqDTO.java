package com.welfare.service.remote.entity;

import com.welfare.common.enums.ShoppingActionTypeEnum;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 10:57
 */
@Data
public class EmployerReqDTO {
  private ShoppingActionTypeEnum actionType;
  private List<EmployerDTO> list;
  private String requestId;
  private String timestamp;
}
