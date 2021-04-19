package com.welfare.service.dto.offline;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 2:00 下午
 */
@Data
@ExcelIgnoreUnannotated
public class OfflineOrderExcelDTO {

  @ExcelProperty(value = "交易时间", index = 0)
  private String tradeTime;

  @ExcelProperty(value = "门店名称", index = 1)
  private String storeName;

  @ExcelProperty(value = "部门名称", index = 2)
  private String departmentName;

  @ExcelProperty(value = "帐户名称", index = 3)
  private String accountName;

  @ExcelProperty(value = "手机号", index = 4)
  private String phone;

  @ExcelProperty(value = "交易金额", index = 5)
  private BigDecimal amount;

  @ExcelProperty(value = "状态名称", index = 6)
  private String statusName;

  public static List<OfflineOrderExcelDTO> of(List<OfflineOrderDTO> dtos) {
    List<OfflineOrderExcelDTO> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(dtos)) {
      dtos.forEach(offlineOrderDTO -> {
        OfflineOrderExcelDTO excelDTO = new OfflineOrderExcelDTO();
        BeanUtils.copyProperties(offlineOrderDTO, excelDTO);
        excelDTO.setAmount(new BigDecimal(offlineOrderDTO.getAmount()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
        list.add(excelDTO);
      });
    }
    return list;
  }
}
