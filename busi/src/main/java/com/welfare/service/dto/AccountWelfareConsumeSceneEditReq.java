package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 1:15 下午
 */
@Data
public class AccountWelfareConsumeSceneEditReq {

  /**
   * 场景ID
   */
  @ApiModelProperty("id")
  private String id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码为空")
  private String merCode;

  /**
   * 员工类型编码
   */
  @ApiModelProperty("福利类型编码")
  @NotEmpty(message = "福利类型为空")
  private String accountTypeCode;

  /**
   * 对应消费门店配置
   */
  @ApiModelProperty("对应消费门店配置")
  private List<AccountConsumeStoreRelationEditReq> accountConsumeStoreRelationEditReqs;
}
