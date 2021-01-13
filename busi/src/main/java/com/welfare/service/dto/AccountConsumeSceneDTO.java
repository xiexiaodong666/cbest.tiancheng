package com.welfare.service.dto;

import com.welfare.persist.dto.AccountConsumeStoreRelationDTO;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 15:02
 */
@Data
public class AccountConsumeSceneDTO {
  /**
   * 商户代码
   */
  private String merCode;
  /**
   * 员工类型ID
   */
  private String accountTypeCode;
  /**
   * 员工类型名称
   */
  private String accountTypeName;
  /**
   * 消费配置配置门店
   */
  private List<AccountConsumeStoreRelationDTO> accountConsumeStoreRelationDTOListDTOList;

  /**
   * 备注
   */
  private String remark;
}
