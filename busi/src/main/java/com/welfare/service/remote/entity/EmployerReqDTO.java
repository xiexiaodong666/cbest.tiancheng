package com.welfare.service.remote.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 10:57
 */
@Data
public class EmployerReqDTO {
  private List<EmployerDTO> list;
  private String requestId;
  private Date timestamp;
}
