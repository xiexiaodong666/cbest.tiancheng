package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.dto.AccountRestoreCreditLimitReq;
import com.welfare.service.remote.config.FeignConfiguration;
import com.welfare.service.remote.entity.WelfareResp;
import com.welfare.service.remote.fallback.AccountCreditFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/10 1:07 下午
 */
@ConditionalOnHavingProperty("e-welfare.account.url")
@FeignClient(value = "accountCredit", url = "${e-welfare.account.url}", fallbackFactory = AccountCreditFeignFallback.class, configuration = {FeignConfiguration.class})
public interface AccountCreditFeign {

  /**
   * 调用恢复员工信用额度
   */
  @RequestMapping(value = "/accountCredit/batchRestore", method = RequestMethod.POST, consumes = "application/json")
  WelfareResp remainingLimit(@RequestBody AccountRestoreCreditLimitReq request, @RequestHeader(name = "Source",required = true) String source);

}
