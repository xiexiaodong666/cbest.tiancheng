package com.welfare.service.dto.merchantconsume;

import com.welfare.common.constants.WelfareSettleConstant.BusinessTypeEnum;
import com.welfare.common.enums.MerCooperationModeEnum;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataBaiscResponse;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 11:32 AM
 */
@Data
public class MerChantConsumeDataRowsApiResponse {

  @ApiModelProperty("虚拟id")
  private Long id;

  @ApiModelProperty("商户代码")
  private String merCode;

  @ApiModelProperty("商户名称")
  private String merName;

  @ApiModelProperty("员工人数")
  private String userNum;

  @ApiModelProperty("付费类型")
  private String merCooperationMode;

  @ApiModelProperty("商户余额")
  private String currentBalance;

  @ApiModelProperty("信用额度")
  private String creditLimit;

  @ApiModelProperty("剩余信用额度/信用额度")
  private String remainingLimit;

  @ApiModelProperty("结算额度")
  private String settledMoney;

  @ApiModelProperty("待结算额度")
  private String unsettledMoney;

  @ApiModelProperty("业务类型")
  private String businessType;

  @ApiModelProperty("消费总金额")
  private String consumeMoney;

  @ApiModelProperty("百货消费总金额")
  private String bhConsumeMoney;

  @ApiModelProperty("超市消费总金额")
  private String dqConsumeMoney;

  @ApiModelProperty("电器消费总金额")
  private String csConsumeMoney;


  @ApiModelProperty("消费人数")
  private Integer consumePeopleNum;

  @ApiModelProperty("交易笔数")
  private Integer transNum;

  @ApiModelProperty("人均消费")
  private String avgPeopleConsumeMoney;

  @ApiModelProperty("每笔平均交易额")
  private String avgTransMoney;

  @ApiModelProperty("累计充值额度")
  private String chargeBalance;

  @ApiModelProperty("在相应时间内访问app的人数")
  private String visitAppNum;

  @ApiModelProperty("在相应时间内每天访问app的人数")
  private String avgVisitAppNum;

  @ApiModelProperty("消费方式明细集合")
  private List<MerChantConsumeDataDetailApiResponse> consumeTypeDetailList;

  public static MerChantConsumeDataRowsApiResponse selfOf(WelfareMerChantConsumeDataBaiscResponse response) {
    MerChantConsumeDataRowsApiResponse merChantConsumeDataRowsApiResponse = new MerChantConsumeDataRowsApiResponse();
    merChantConsumeDataRowsApiResponse.setMerCode(response.getMerCode());
    merChantConsumeDataRowsApiResponse.setMerName(response.getMerName());
    merChantConsumeDataRowsApiResponse.setUserNum(response.getUserNum());
    merChantConsumeDataRowsApiResponse.setChargeBalance(response.getChargeBalance());
    merChantConsumeDataRowsApiResponse.setVisitAppNum(response.getVisitAppNum());
    merChantConsumeDataRowsApiResponse.setAvgVisitAppNum(response.getAvgVisitAppNum());
    merChantConsumeDataRowsApiResponse.setMerCooperationMode(MerCooperationModeEnum.getByCode(response.getMerCooperationMode()).getDesc());
    merChantConsumeDataRowsApiResponse.setCurrentBalance(response.getCurrentBalance());
    merChantConsumeDataRowsApiResponse.setCreditLimit(response.getCreditLimit());
    merChantConsumeDataRowsApiResponse.setRemainingLimit(response.getRemainingLimit());
    merChantConsumeDataRowsApiResponse.setSettledMoney(response.getSettledMoney());
    merChantConsumeDataRowsApiResponse.setUnsettledMoney(response.getUnsettledMoney());
    merChantConsumeDataRowsApiResponse.setBusinessType(BusinessTypeEnum.valueOf(response.getBusinessType().toUpperCase()).desc());
    merChantConsumeDataRowsApiResponse.setConsumeMoney(response.getConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setCsConsumeMoney("-");
    merChantConsumeDataRowsApiResponse.setBhConsumeMoney("-");
    merChantConsumeDataRowsApiResponse.setDqConsumeMoney("-");
    merChantConsumeDataRowsApiResponse.setConsumePeopleNum(response.getConsumePeopleNumCollect());
    merChantConsumeDataRowsApiResponse.setTransNum(response.getTransNumCollect());
    merChantConsumeDataRowsApiResponse.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setAvgTransMoney(response.getAvgTransMoneyCollect());

    return merChantConsumeDataRowsApiResponse;
  }

  public static MerChantConsumeDataRowsApiResponse thirdOf(WelfareMerChantConsumeDataBaiscResponse response, boolean isFillMerchantAttributes) {
    MerChantConsumeDataRowsApiResponse merChantConsumeDataRowsApiResponse = new MerChantConsumeDataRowsApiResponse();

    if(isFillMerchantAttributes) {
      merChantConsumeDataRowsApiResponse.setMerCode(response.getMerCode());
      merChantConsumeDataRowsApiResponse.setMerName(response.getMerName());
      merChantConsumeDataRowsApiResponse.setUserNum(response.getUserNum());
      merChantConsumeDataRowsApiResponse.setChargeBalance(response.getChargeBalance());
      merChantConsumeDataRowsApiResponse.setVisitAppNum(response.getVisitAppNum());
      merChantConsumeDataRowsApiResponse.setAvgVisitAppNum(response.getAvgVisitAppNum());
      merChantConsumeDataRowsApiResponse.setMerCooperationMode(MerCooperationModeEnum.getByCode(response.getMerCooperationMode()).getDesc());
      merChantConsumeDataRowsApiResponse.setCurrentBalance(response.getCurrentBalance());
      merChantConsumeDataRowsApiResponse.setCreditLimit(response.getCreditLimit());
      merChantConsumeDataRowsApiResponse.setRemainingLimit(response.getRemainingLimit());
      merChantConsumeDataRowsApiResponse.setSettledMoney(response.getSettledMoney());
      merChantConsumeDataRowsApiResponse.setUnsettledMoney(response.getUnsettledMoney());

    }
    merChantConsumeDataRowsApiResponse.setBusinessType(BusinessTypeEnum.valueOf(response.getBusinessType().toUpperCase()).desc());
    merChantConsumeDataRowsApiResponse.setConsumeMoney(response.getConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setCsConsumeMoney(response.getCsConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setBhConsumeMoney(response.getBhConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setDqConsumeMoney(response.getDqConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setConsumePeopleNum(response.getConsumePeopleNumCollect());
    merChantConsumeDataRowsApiResponse.setTransNum(response.getTransNumCollect());
    merChantConsumeDataRowsApiResponse.setAvgPeopleConsumeMoney(response.getAvgPeopleConsumeMoneyCollect());
    merChantConsumeDataRowsApiResponse.setAvgTransMoney(response.getAvgTransMoneyCollect());

    return merChantConsumeDataRowsApiResponse;
  }
}
