package com.welfare.service.dto;

import com.welfare.persist.entity.EmployeeSettle;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/10 1:18 下午
 */
@Data
public class AccountRestoreCreditLimitReq {

  private List<RestoreCreditLimitDTO> creditLimitDtos;

  @Data
  public static class RestoreCreditLimitDTO{

    @NotEmpty(message = "流水号不能为空")
    private String settleNo;

    @NotNull(message = "恢复金额不能为空")
    @DecimalMin(value = "0", message = "金额不能小于0")
    private BigDecimal restoreAmount;

    @NotNull(message = "员工编码不能为空")
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
}
