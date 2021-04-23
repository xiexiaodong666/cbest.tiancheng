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

  @ApiModelProperty("商户ID")
  @ExcelProperty(value = "商户ID")
  @ColumnWidth(10)
  private String merCode;

  @ApiModelProperty("商户名称")
  @ExcelProperty(value = "商户名称")
  @ColumnWidth(30)
  private String merName;

  @ApiModelProperty("员工数（人）")
  @ExcelProperty(value = "员工数（人）")
  @ColumnWidth(10)
  private String userNum;

  @ApiModelProperty("付费类型")
  @ExcelProperty(value = "付费类型")
  @ColumnWidth(10)
  private String merCooperationMode;

  @ApiModelProperty("商户余额（元）")
  @ExcelProperty(value = "商户余额（元）")
  @ColumnWidth(20)
  private String currentBalance;

  @ApiModelProperty("剩余信用额度/信用额度（元）")
  @ExcelProperty(value = "剩余信用额度/信用额度（元）")
  @ColumnWidth(20)
  private String remainingLimit;

  @ApiModelProperty("累计结算金额（元）")
  @ExcelProperty(value = "累计结算金额（元）")
  @ColumnWidth(20)
  private String settledMoney;

  @ApiModelProperty("未结算金额（元）\n (包含待结算及结算中)")
  @ExcelProperty(value = "未结算金额（元）\n (包含待结算及结算中)")
  @ColumnWidth(20)
  private String unsettledMoney;

  @ApiModelProperty("业务类型")
  @ExcelProperty(value = "业务类型")
  @ColumnWidth(20)
  private String businessType;

  @ApiModelProperty("消费方式")
  @ExcelProperty(value = "消费方式")
  @ColumnWidth(30)
  private String consumeType;

  @ApiModelProperty("消费总金额（元）")
  @ExcelProperty(value = "消费总金额（元）")
  @ColumnWidth(15)
  private String consumeMoney;


  @ApiModelProperty("百货消费总金额（元）")
  @ExcelProperty(value = "百货消费总金额（元）")
  @ColumnWidth(15)
  private String bhConsumeMoney;

  @ApiModelProperty("超市消费总金额（元）")
  @ExcelProperty(value = "超市消费总金额（元）")
  @ColumnWidth(15)
  private String csConsumeMoney;

  @ApiModelProperty("电器消费总金额（元）")
  @ExcelProperty(value = "电器消费总金额（元）")
  @ColumnWidth(15)
  private String dqConsumeMoney;


  @ApiModelProperty("消费人数（人）")
  @ExcelProperty(value = "消费人数（人）")
  @ColumnWidth(15)
  private Integer consumePeopleNum;

  @ApiModelProperty("交易笔数（笔）")
  @ExcelProperty(value = "交易笔数（笔）")
  @ColumnWidth(15)
  private Integer transNum;

  @ApiModelProperty("人均消费金额（元）")
  @ExcelProperty(value = "人均消费金额（元）")
  @ColumnWidth(15)
  private String avgPeopleConsumeMoney;

  @ApiModelProperty("每笔交易平均金额（元）")
  @ExcelProperty(value = "每笔交易平均金额（元）")
  @ColumnWidth(15)
  private String avgTransMoney;

  public static ExportMerChantConsumeData rowsOf(MerChantConsumeDataRowsApiResponse response) {

    ExportMerChantConsumeData exportMerChantConsumeData = new ExportMerChantConsumeData();

    exportMerChantConsumeData.setMerCode(response.getMerCode());
    exportMerChantConsumeData.setMerName(response.getMerName());
    exportMerChantConsumeData.setUserNum(response.getUserNum());
    exportMerChantConsumeData.setMerCooperationMode(response.getMerCooperationMode());
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
    exportMerChantConsumeData.setBhConsumeMoney(response.getBhConsumeMoney());
    exportMerChantConsumeData.setCsConsumeMoney(response.getCsConsumeMoney());
    exportMerChantConsumeData.setDqConsumeMoney(response.getDqConsumeMoney());

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
    exportMerChantConsumeData.setBhConsumeMoney(response.getBhConsumeMoney());
    exportMerChantConsumeData.setCsConsumeMoney(response.getCsConsumeMoney());
    exportMerChantConsumeData.setDqConsumeMoney(response.getDqConsumeMoney());

    return exportMerChantConsumeData;
  }



  public static ExportMerChantConsumeData extOf(TableExt response) {

    ExportMerChantConsumeData exportMerChantConsumeData = new ExportMerChantConsumeData();

    //exportMerChantConsumeData.setMerCode("汇总");
    exportMerChantConsumeData.setMerName(response.getMerName());
    exportMerChantConsumeData.setUserNum(response.getUserNum());
    exportMerChantConsumeData.setMerCooperationMode(response.getMerCooperationMode());
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

    exportMerChantConsumeData.setBhConsumeMoney(response.getBhConsumeMoney());
    exportMerChantConsumeData.setCsConsumeMoney(response.getCsConsumeMoney());
    exportMerChantConsumeData.setDqConsumeMoney(response.getDqConsumeMoney());

    return exportMerChantConsumeData;
  }
}
