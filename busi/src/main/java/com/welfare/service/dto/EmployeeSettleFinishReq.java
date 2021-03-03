package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:45 下午
 */
@Data
@ApiModel("完成结算请求对象")
public class EmployeeSettleFinishReq {

  @ApiModelProperty("结算单号")
  @NotEmpty(message = "至少勾选一个结算单！")
  private List<String> settleNos;
}
