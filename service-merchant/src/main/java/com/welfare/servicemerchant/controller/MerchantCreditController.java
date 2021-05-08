package com.welfare.servicemerchant.controller;

import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.service.AccountService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户额度信服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchantCredit")
@Api(tags = "商户额度信服务")
public class MerchantCreditController implements IController {

    private final MerchantCreditService merchantCreditService;

    private final AccountService accountService;

    @PostMapping("/restore/remainingLimit")
    @ApiOperation("恢复商户剩余信用额度")
    public R<String> save(@Validated @RequestBody RestoreRemainingLimitReq request){
        if(MerCreditType.WHOLESALE_CREDIT_LIMIT.code().equals(request.getSettleType())) {
            merchantCreditService.restoreWholesaleCreditLimit(request);
        } else {
            merchantCreditService.restoreRemainingLimit(request);
        }
        return success();
    }
}