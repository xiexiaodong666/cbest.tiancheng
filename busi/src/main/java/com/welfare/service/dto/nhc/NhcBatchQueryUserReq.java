package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 6:01 下午
 */
@Data
public class NhcBatchQueryUserReq {

  @ApiModelProperty("用户编码")
  private List<String> accountCodes;
}
