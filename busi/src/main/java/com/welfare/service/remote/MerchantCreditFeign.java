package com.welfare.service.remote;

import com.baomidou.mybatisplus.extension.api.R;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import com.welfare.service.remote.entity.*;
import com.welfare.service.remote.fallback.MerchantCreditFallback;
import com.welfare.service.remote.fallback.ShoppingFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "=merchantCredit", url = "${merchant.url:http://172.30.37.188:10016}", fallbackFactory = MerchantCreditFallback.class)
public interface MerchantCreditFeign {


  /**
   * 调用恢复信用额度
   */
  @RequestMapping(value = "/merchantCredit/restore/remainingLimit", method = RequestMethod.POST, consumes = "application/json")
  MerchantCreditResp remainingLimit(@RequestBody RestoreRemainingLimitReq request);
}
