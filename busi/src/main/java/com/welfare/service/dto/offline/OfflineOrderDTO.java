package com.welfare.service.dto.offline;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 2:55 下午
 */
@Data
@ExcelIgnoreUnannotated
public class OfflineOrderDTO {

  @ExcelProperty(value = "交易时间", index = 0)
  @ApiModelProperty("交易时间，格式“yyyy-MM-dd HH:mm:ss”")
  private String tradeTime;

  @ExcelProperty(value = "门店名称", index = 1)
  @ApiModelProperty("门店名称")
  private String storeName;

  @ExcelProperty(value = "部门名称", index = 2)
  @ApiModelProperty("部门名称")
  private String departmentName;

  @ExcelProperty(value = "帐户名称", index = 3)
  @ApiModelProperty("帐户名称")
  private String accountName;

  @ExcelProperty(value = "手机号", index = 4)
  @ApiModelProperty("手机号")
  private String phone;

  @ExcelProperty(value = "消费金额", index = 5)
  @ApiModelProperty("消费金额，单位")
  private Integer amount;

  @ExcelProperty(value = "状态名称", index = 6)
  @ApiModelProperty("状态名称")
  private String statusName;

  @Data
  @ExcelIgnoreUnannotated
  public static class OfflineOrderDTO2{

    @ExcelProperty(value = "交易时间", index = 0)
    @ApiModelProperty("交易时间，格式“yyyy-MM-dd HH:mm:ss”")
    private String tradeTime;

    @ExcelProperty(value = "门店名称", index = 1)
    @ApiModelProperty("门店名称")
    private String storeName;

    @ExcelProperty(value = "部门名称", index = 2)
    @ApiModelProperty("部门名称")
    private String departmentName;

    @ExcelProperty(value = "帐户名称", index = 3)
    @ApiModelProperty("帐户名称")
    private String accountName;

    @ExcelProperty(value = "手机号", index = 4)
    @ApiModelProperty("手机号")
    private String phone;

    @ExcelProperty(value = "消费金额", index = 5)
    @ApiModelProperty("消费金额，单位")
    private BigDecimal amount;

    @ExcelProperty(value = "状态名称", index = 6)
    @ApiModelProperty("状态名称")
    private String statusName;
  }
}
