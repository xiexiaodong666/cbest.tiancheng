package com.welfare.service.remote.entity.pos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/12  3:15 PM
 */
@Data
@NoArgsConstructor
public class PagingResult<T> {
  private Integer pageNo;
  private Integer pageSize;
  private Integer total;
  private List<T> list;
}