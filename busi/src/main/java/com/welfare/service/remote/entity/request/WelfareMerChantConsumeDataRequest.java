package com.welfare.service.remote.entity.request;

import com.welfare.common.util.DateUtil;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiRequest;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:13 AM
 */
@Data
public class WelfareMerChantConsumeDataRequest {

  /**
   * 业务类型
   */
  private String businessType;
  /**
   * 消费方式
   */
  private String consumeType;
  /**
   * 消费时间筛选结束
   */
  private String endDate;
  /**
   * 消费时间筛选起始
   */
  private String startDate;
  /**
   * 商户代码
   */
  private String merCode;

  public static WelfareMerChantConsumeDataRequest of(WelfareMerChantConsumeDataApiRequest request) {

    WelfareMerChantConsumeDataRequest welfareMerChantConsumeDataRequest = new WelfareMerChantConsumeDataRequest();

    welfareMerChantConsumeDataRequest.setBusinessType(request.getBusinessType());
    welfareMerChantConsumeDataRequest.setConsumeType(request.getConsumeType());
    welfareMerChantConsumeDataRequest.setEndDate(
        DateUtil.date2Str(request.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT));
    welfareMerChantConsumeDataRequest.setStartDate(
        DateUtil.date2Str(request.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT));
    welfareMerChantConsumeDataRequest.setMerCode(request.getMerCode());

    return welfareMerChantConsumeDataRequest;
  }
}
