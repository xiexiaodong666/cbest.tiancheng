package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/12  3:13 PM
 */
@Data
@NoArgsConstructor
public class PagingCondition {
  private Integer pageNo;
  private Integer pageSize;
  @ApiModelProperty("排序字段")
  private Integer orderField;
  @ApiModelProperty("排序方式 0.顺序 1.倒序")
  private Integer orderby;
}