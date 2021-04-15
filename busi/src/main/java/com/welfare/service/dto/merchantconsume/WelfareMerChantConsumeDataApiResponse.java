package com.welfare.service.dto.merchantconsume;

import com.welfare.common.constants.WelfareSettleConstant.BusinessTypeEnum;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataBaiscResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:51 AM
 */
@Data
public class WelfareMerChantConsumeDataApiResponse {


  @ApiModelProperty("列表数据")
  List<MerChantConsumeDataRowsApiResponse> rowsData;

  @ApiModelProperty("顶部汇总数据")
  private  TopExt topExt;
  @ApiModelProperty("列表汇总数据")
  private  List<TableExt> tableExt;


  @ApiModel("顶部汇总数据")
  @Data
  public static class TopExt {

    @ApiModelProperty("消费商户数")
    private Integer allMerNum;
    @ApiModelProperty("企业福利（自营）业务消费商户数")
    private Integer selfMerNum;
    @ApiModelProperty("企业福利（非自营）业务消费商户数：2")
    private Integer thirdMerNum;
  }

  @ApiModel("列表汇总数据")
  @Data
  public static class TableExt {
    @ApiModelProperty("商户名称")
    private String merName;

    @ApiModelProperty("员工人数")
    private String userNum;

    @ApiModelProperty("商户余额")
    private String currentBalance;

    @ApiModelProperty("剩余信用额度/信用额度")
    private String remainingLimit;

    @ApiModelProperty("结算额度")
    private String settledMoney;

    @ApiModelProperty("待结算额度")
    private String unsettledMoney;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("消费总金额")
    private Double consumeMoney;

    @ApiModelProperty("消费人数")
    private Integer consumePeopleNum;

    @ApiModelProperty("交易笔数")
    private Integer transNum;

    @ApiModelProperty("人均消费")
    private Double avgPeopleConsumeMoney;

    @ApiModelProperty("每笔平均交易额")
    private Double avgTransMoney;

    @ApiModelProperty("消费方式明细集合")
    private List<MerChantConsumeDataDetailApiResponse> consumeTypeDetailList;

    public static TableExt selfOf(WelfareMerChantConsumeDataBaiscResponse response) {
      TableExt tableExt = new TableExt();
      tableExt.setMerName("汇总");
      tableExt.setUserNum(response.getUserNum());
      tableExt.setCurrentBalance(response.getCurrentBalance());
      tableExt.setRemainingLimit(response.getRemainingLimit());
      tableExt.setSettledMoney(response.getSettledMoney());
      tableExt.setUnsettledMoney(response.getUnsettledMoney());
      tableExt.setBusinessType(BusinessTypeEnum.valueOf(response.getBusinessType().toUpperCase()).desc());
      tableExt.setConsumeMoney(response.getConsumeMoneyCollect());
      tableExt.setConsumePeopleNum(response.getConsumePeopleNumCollect());
      tableExt.setTransNum(response.getTransNumCollect());
      tableExt.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoneyCollect());
      tableExt.setAvgTransMoney(response.getAvgTransMoneyCollect());

      return tableExt;
    }

    public static TableExt thirdOf(WelfareMerChantConsumeDataBaiscResponse response) {
      TableExt tableExt = new TableExt();

      tableExt.setBusinessType(BusinessTypeEnum.valueOf(response.getBusinessType().toUpperCase()).desc());
      tableExt.setConsumeMoney(response.getConsumeMoneyCollect());
      tableExt.setConsumePeopleNum(response.getConsumePeopleNumCollect());
      tableExt.setTransNum(response.getTransNumCollect());
      tableExt.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoneyCollect());
      tableExt.setAvgTransMoney(response.getAvgTransMoneyCollect());

      return tableExt;
    }

  }

}
