package com.welfare.service.remote.entity;

import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 10:12 AM
 */
@Data
public class PlatformUserDataResponse<T> {

  private Integer total;
  private List<T> list;
}
