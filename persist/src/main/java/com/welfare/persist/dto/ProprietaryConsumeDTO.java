package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 5:24 下午
 */
@Data
public class ProprietaryConsumeDTO {

  @ExcelProperty(value = "序号")
  @ApiModelProperty(value = "序号")
  private Long id;

  @ExcelProperty(value = "交易流水号")
  @ApiModelProperty(value = "交易流水号")
  private String transNo;

  @ExcelProperty(value = "订单号")
  @ApiModelProperty(value = "订单号")
  private String orderNO;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ExcelProperty(value = "交易时间")
  @ApiModelProperty(value = "交易时间")
  private Date transTime;

  @ExcelProperty(value = "消费人名称")
  @ApiModelProperty(value = "消费人名称")
  private String accountName;

  @ApiModelProperty(value = "消费人编码")
  private String accountCode;

  @ExcelProperty(value = "手机号")
  @ApiModelProperty(value = "手机号")
  private String phone;

  @ApiModelProperty(value = "组织机构代码")
  private String departmentCode;

  @ExcelProperty(value = "组织机构名称")
  @ApiModelProperty(value = "组织机构名称")
  private String departmentName;

  @ApiModelProperty(value = "消费门店编号")
  private String storeCode;

  @ExcelProperty(value = "消费门店名称")
  @ApiModelProperty(value = "消费门店名称")
  private String storeNam;

  @ApiModelProperty(value = "员工支出方式编码")
  private String welfareTypeCode;

  @ExcelProperty(value = "员工支出方式名称")
  @ApiModelProperty(value = "员工支出方式名称")
  private String welfareTypeName;

  @ExcelProperty(value = "消费金额")
  @ApiModelProperty(value = "消费金额")
  private String payAmount;
}
