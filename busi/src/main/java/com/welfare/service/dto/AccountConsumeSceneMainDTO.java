package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 3:16 下午
 */
@Data
public class AccountConsumeSceneMainDTO {

  private Long id;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;

  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;

  @ApiModelProperty("员工类型消费配置")
  private List<AccountConsumeSceneDTO> consumeStoreRelationDTOS;
}
