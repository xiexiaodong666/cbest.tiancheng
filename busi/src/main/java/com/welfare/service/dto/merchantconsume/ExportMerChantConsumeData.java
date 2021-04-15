package com.welfare.service.dto.merchantconsume;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiResponse.TableExt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 11:32 AM
 */
@Data
public class ExportMerChantConsumeData {

  @ApiModelProperty("商户代码")
  @ExcelProperty(value = "商户代码")
  @ColumnWidth(10)
  private String merCode;

  @ApiModelProperty("商户名称")
  @ExcelProperty(value = "商户名称")
  @ColumnWidth(10)
  private String merName;

  @ApiModelProperty("员工人数")
  @ExcelProperty(value = "员工人数")
  @ColumnWidth(10)
  private String userNum;

  @ApiModelProperty("商户余额")
  @ExcelProperty(value = "商户余额")
  @ColumnWidth(10)
  private String currentBalance;

  @ApiModelProperty("剩余信用额度/信用额度")
  @ExcelProperty(value = "剩余信用额度/信用额度")
  @ColumnWidth(10)
  private String remainingLimit;

  @ApiModelProperty("结算额度")
  @ExcelProperty(value = "结算额度")
  @ColumnWidth(10)
  private String settledMoney;

  @ApiModelProperty("待结算额度")
  @ExcelProperty(value = "待结算额度")
  @ColumnWidth(10)
  private String unsettledMoney;

  @ApiModelProperty("业务类型")
  @ExcelProperty(value = "业务类型")
  @ColumnWidth(30)
  private String businessType;

  @ApiModelProperty("消费方式")
  @ExcelProperty(value = "消费方式")
  @ColumnWidth(30)
  private String consumeType;

  @ApiModelProperty("消费总金额")
  @ExcelProperty(value = "消费总金额")
  @ColumnWidth(10)
  private Double consumeMoney;

  @ApiModelProperty("消费人数")
  @ExcelProperty(value = "消费人数")
  @ColumnWidth(10)
  private Integer consumePeopleNum;

  @ApiModelProperty("交易笔数")
  @ExcelProperty(value = "交易笔数")
  @ColumnWidth(10)
  private Integer transNum;

  @ApiModelProperty("人均消费")
  @ExcelProperty(value = "人均消费")
  @ColumnWidth(10)
  private Double avgPeopleConsumeMoney;

  @ApiModelProperty("每笔平均交易额")
  @ExcelProperty(value = "每笔平均交易额")
  @ColumnWidth(10)
  private Double avgTransMoney;

  public static ExportMerChantConsumeData rowsOf(MerChantConsumeDataRowsApiResponse response) {

    ExportMerChantConsumeData exportMerChantConsumeData = new ExportMerChantConsumeData();

    exportMerChantConsumeData.setMerCode(response.getMerCode());
    exportMerChantConsumeData.setMerName(response.getMerName());
    exportMerChantConsumeData.setUserNum(response.getUserNum());
    exportMerChantConsumeData.setCurrentBalance(response.getCurrentBalance());
    exportMerChantConsumeData.setRemainingLimit(response.getRemainingLimit());
    exportMerChantConsumeData.setSettledMoney(response.getSettledMoney());
    exportMerChantConsumeData.setUnsettledMoney(response.getUnsettledMoney());
    exportMerChantConsumeData.setBusinessType(response.getBusinessType());
    exportMerChantConsumeData.setConsumeType("-");
    exportMerChantConsumeData.setConsumeMoney(response.getConsumeMoney());
    exportMerChantConsumeData.setConsumePeopleNum(response.getConsumePeopleNum());
    exportMerChantConsumeData.setTransNum(response.getTransNum());
    exportMerChantConsumeData.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoney());
    exportMerChantConsumeData.setAvgTransMoney(response.getAvgTransMoney());

    return exportMerChantConsumeData;
  }

  public static ExportMerChantConsumeData detailOf(MerChantConsumeDataDetailApiResponse response) {

    ExportMerChantConsumeData exportMerChantConsumeData = new ExportMerChantConsumeData();

    exportMerChantConsumeData.setConsumeType(response.getConsumeType());
    exportMerChantConsumeData.setConsumeMoney(response.getConsumeMoney());
    exportMerChantConsumeData.setConsumePeopleNum(response.getConsumePeopleNum());
    exportMerChantConsumeData.setTransNum(response.getTransNum());
    exportMerChantConsumeData.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoney());
    exportMerChantConsumeData.setAvgTransMoney(response.getAvgTransMoney());

    return exportMerChantConsumeData;
  }



  public static ExportMerChantConsumeData extOf(TableExt response) {

    ExportMerChantConsumeData exportMerChantConsumeData = new ExportMerChantConsumeData();

    exportMerChantConsumeData.setMerCode("汇总");
    exportMerChantConsumeData.setMerName(response.getMerName());
    exportMerChantConsumeData.setUserNum(response.getUserNum());
    exportMerChantConsumeData.setCurrentBalance(response.getCurrentBalance());
    exportMerChantConsumeData.setRemainingLimit(response.getRemainingLimit());
    exportMerChantConsumeData.setSettledMoney(response.getSettledMoney());
    exportMerChantConsumeData.setUnsettledMoney(response.getUnsettledMoney());
    exportMerChantConsumeData.setBusinessType(response.getBusinessType());
    exportMerChantConsumeData.setConsumeType("-");
    exportMerChantConsumeData.setConsumeMoney(response.getConsumeMoney());
    exportMerChantConsumeData.setConsumePeopleNum(response.getConsumePeopleNum());
    exportMerChantConsumeData.setTransNum(response.getTransNum());
    exportMerChantConsumeData.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoney());
    exportMerChantConsumeData.setAvgTransMoney(response.getAvgTransMoney());

    return exportMerChantConsumeData;
  }
}
