package com.welfare.service.dto;

import com.welfare.persist.dto.AccountConsumeStoreRelationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 15:02
 */
@Data
@ApiModel("员工类型消费配置")
public class AccountConsumeSceneDTO {
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 员工类型ID
   */
  @ApiModelProperty("员工类型Code")
  private String accountTypeCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;
  /**
   * 消费配置配置门店
   */
  @ApiModelProperty("消费配置配置门店")
  private List<AccountConsumeStoreRelationDTO> accountConsumeStoreRelationDTOListDTOList;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
}
