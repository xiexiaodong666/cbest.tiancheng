package com.welfare.serviceaccount.controller;

import com.welfare.service.AccountDepositApplyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 账户充值申请服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 11:08:59
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountDepositApply")
public class AccountDepositApplyController implements IController {
    private final AccountDepositApplyService accountDepositApplyService;

    @ApiOperation("汇总充值总额")
    @GetMapping("/deposit-summary")
    public R<BigDecimal> sumDeposit(@RequestParam @ApiParam("商户编码") String merCode,
                        @RequestParam @ApiParam("福利类型编码 ") String merAccountTypeCode){
        BigDecimal totalDepositAmount = accountDepositApplyService.sumDepositDetailAmount(merCode,merAccountTypeCode);
        return success(totalDepositAmount);
    }
}