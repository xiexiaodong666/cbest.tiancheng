package com.welfare.serviceaccount.controller;

import com.welfare.common.annotation.AccountUser;
import com.welfare.common.util.AccountUserHolder;
import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import com.welfare.service.dto.CreateThirdPartyPaymentDTO;
import com.welfare.service.dto.CreateThirdPartyPaymentReq;
import com.welfare.service.remote.entity.AlipayUserAgreementQueryResp;
import com.welfare.service.remote.entity.CbestPayBaseResp;
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

    @ApiOperation("第三方支付结果通知")
    @PostMapping("/thirdPartyPaymentResultNotify")
    public R thirdPartyPaymentResultNotify(@RequestBody CbestPayBaseResp resp) {
        accountPaymentResultService.thirdPartyPaymentResultNotify(resp);
        return success();
    }

    @ApiOperation("第三方交易创建通知")
    @PostMapping("/createThirdPartyPaymentNotify")
    public R createThirdPartyPaymentNotify(@RequestBody CbestPayBaseResp resp) {
        accountPaymentResultService.createThirdPartyPaymentNotify(resp);
        return success();
    }

    @ApiOperation("第三方交易创建")
    @GetMapping("/createThirdPartyPayment")
    @AccountUser
    public R<CreateThirdPartyPaymentDTO> createThirdPartyPayment(CreateThirdPartyPaymentReq req) {
        CreateThirdPartyPaymentDTO createThirdPartyPaymentDTO = accountPaymentResultService
            .createThirdPartyPayment(req);
        return success(createThirdPartyPaymentDTO);
    }

    @ApiOperation("第三方签约或解约结果通知")
    @PostMapping("/thirdPartySignResultNotify")
    public R thirdPartySignResultNotify(@RequestBody AlipayUserAgreementQueryResp resp) {
        accountPaymentResultService.thirdPartySignResultNotify(resp);
        return success();
    }

}
