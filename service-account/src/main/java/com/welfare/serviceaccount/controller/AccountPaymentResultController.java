package com.welfare.serviceaccount.controller;

import com.welfare.common.annotation.AccountUser;
import com.welfare.common.util.AccountUserHolder;
import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountPaymentResult")
@Api(tags = "用户支付结果接口")
public class AccountPaymentResultController implements IController {

    @Resource
    private AccountPaymentResultService accountPaymentResultService;

    @ApiOperation("扫码支付结果通知")
    @PostMapping("/barcodePaymentNotify")
    public R barcodePaymentNotify(@RequestBody BarcodePaymentNotifyReq req) {
        accountPaymentResultService.barcodePaymentNotify(req);
        return success();
    }

    @ApiOperation("查询扫码支付结果")
    @GetMapping("/barcodePaymentResult")
    @AccountUser
    public R<BarcodePaymentResultDTO> barcodePaymentResult(BarcodePaymentResultReq req) {
        req.setAccountCode(AccountUserHolder.getAccountUser().getAccountCode());
        return success(accountPaymentResultService.queryBarcodePaymentResult(req));
    }

}
