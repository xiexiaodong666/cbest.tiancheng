package com.welfare.service.dto;

import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.EmployeeSettle;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 结算后恢复员工授信额度对象
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/8 6:30 下午
 */
@Data
public class RestoreCreditLimitDTO {

  private String settleNo;

  private BigDecimal restoreAmount;

  private Long accountCode;

  public static List<RestoreCreditLimitDTO> of(List<EmployeeSettle> employeeSettles) {
    List<RestoreCreditLimitDTO> creditLimitDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(employeeSettles)) {
      employeeSettles.forEach(employeeSettle -> {
        RestoreCreditLimitDTO restoreCreditLimitDTO = new RestoreCreditLimitDTO();
        restoreCreditLimitDTO.setSettleNo(employeeSettle.getSettleNo());
        restoreCreditLimitDTO.setAccountCode(employeeSettle.getAccountCode());
        restoreCreditLimitDTO.setRestoreAmount(employeeSettle.getSettleAmount());
        creditLimitDtos.add(restoreCreditLimitDTO);
      });
    }
    return creditLimitDtos;
  }
}
