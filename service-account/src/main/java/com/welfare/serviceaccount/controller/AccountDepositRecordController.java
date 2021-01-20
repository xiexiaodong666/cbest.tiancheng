package com.welfare.serviceaccount.controller;

import com.welfare.common.annotation.AccountUser;
import com.welfare.service.AccountDepositRecordService;
import com.welfare.service.dto.AccountDepositDTO;
import com.welfare.service.dto.AccountDepositReq;
import com.welfare.service.dto.AccountPayResultQueryDTO;
import com.welfare.service.dto.AccountPayResultQueryReq;
import com.welfare.service.remote.entity.CbestPayBaseResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * 账号充值记录表服务控制器
 *
 * @author kancy
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-11 09:20:53
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountDepositRecord")
@Api(tags = "用户充值支付相关接口")
public class AccountDepositRecordController implements IController {

    private final AccountDepositRecordService accountDepositRecordService;

    @ApiOperation("查询支付信息")
    @PostMapping("/payInfo")
    @AccountUser
    public R<AccountDepositDTO> payInfo(@RequestBody AccountDepositReq req) {
        return success(accountDepositRecordService.getPayInfo(req));
    }

    @ApiOperation("查询支付结果")
    @GetMapping("/payResult")
    @AccountUser
    public R<AccountPayResultQueryDTO> payResult(AccountPayResultQueryReq req) {
        return success(accountDepositRecordService.queryPayResult(req));
    }

    @ApiOperation("支付回调")
    @PostMapping("/payNotify")
    public R payNotify(@RequestBody CbestPayBaseResp resp) {
        accountDepositRecordService.payNotify(resp);
        return R.success();
    }
}