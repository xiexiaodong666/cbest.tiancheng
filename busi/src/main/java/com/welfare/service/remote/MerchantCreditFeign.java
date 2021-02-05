package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import com.welfare.service.remote.entity.MerchantCreditResp;
import com.welfare.service.remote.fallback.MerchantCreditFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "merchantCredit", url = "${merchant.url:http://172.30.37.188:10016}", fallbackFactory = MerchantCreditFallback.class)
@ConditionalOnHavingProperty("merchant.url")
public interface MerchantCreditFeign {


  /**
   * 调用恢复信用额度
   */
  @RequestMapping(value = "/merchantCredit/restore/remainingLimit", method = RequestMethod.POST, consumes = "application/json")
  MerchantCreditResp remainingLimit(@RequestBody RestoreRemainingLimitReq request, @RequestHeader(name = "Source",required = true) String source);
}
