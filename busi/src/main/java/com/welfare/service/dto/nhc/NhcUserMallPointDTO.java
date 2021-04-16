package com.welfare.service.dto.nhc;

import com.welfare.persist.entity.AccountAmountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 5:57 下午
 */
@Data
public class NhcUserMallPointDTO {

  @ApiModelProperty("用户编码")
  private String accountCode;

  @ApiModelProperty("用户积分")
  private BigDecimal mallPoint;

  public static List<NhcUserMallPointDTO> of(List<AccountAmountType> accountAmountTypes) {
    List<NhcUserMallPointDTO> mallPointList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(accountAmountTypes)) {
      accountAmountTypes.forEach(accountAmountType -> {
        NhcUserMallPointDTO mallPointDTO = new NhcUserMallPointDTO();
        mallPointDTO.setMallPoint(accountAmountType.getAccountBalance());
        mallPointDTO.setAccountCode(String.valueOf(accountAmountType.getAccountCode()));
      });
    }
    return mallPointList;
  }
}
