package com.welfare.serviceaccount.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.common.annotation.AccountUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.AccountUserHolder;
import com.welfare.persist.dto.AccountConsumeSceneDO;
import com.welfare.persist.dto.AccountOverviewDTO;
import com.welfare.persist.dto.AccountPasswordFreePageSignDTO;
import com.welfare.persist.dto.AccountPasswordFreeSignDTO;
import com.welfare.persist.dto.AccountPaymentChannelDTO;
import com.welfare.persist.dto.AccountSimpleDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.AccountService;
import com.welfare.service.dto.AccountDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账户信息服务控制器
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 11:08:59
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Api(tags = "员工账号管理")
public class AccountController implements IController {

    private final AccountService accountService;

    @ApiOperation("账号信息")
    @GetMapping("/info")
    @AccountUser
    public R<AccountSimpleDTO> info() {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.queryAccountInfo(accountCode));
    }

    @ApiOperation("账号概览信息")
    @GetMapping("/overview")
    @AccountUser
    public R<AccountOverviewDTO> overview(
        @RequestParam(value = "paymentChannel", required = false) @ApiParam("支付渠道") String paymentChannel) {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.queryAccountOverview(accountCode, paymentChannel));
    }

    @ApiOperation("查询账户消费场景-卡内信息")
    @GetMapping("/consume-scene/card")
    public R<AccountConsumeSceneDO> queryAccountConsumeSceneDOByCardInfo(@RequestParam @ApiParam(value = "门店号",required = true) String storeCode,
        @RequestParam @ApiParam(value = "卡内信息", required = true) String cardInsideInfo) {
        AccountConsumeSceneDO accountConsumeSceneDO =
                accountService.queryAccountConsumeSceneDO(storeCode, WelfareConstant.ConsumeQueryType.CARD, cardInsideInfo);
        return success(accountConsumeSceneDO);
    }

    @ApiOperation("查询账户消费场景-条码")
    @GetMapping("/consume-scene/barcode")
    public R<AccountConsumeSceneDO> queryAccountConsumeSceneDOByBarcode(@RequestParam @ApiParam(value = "门店号",required = true) String storeCode,
        @RequestParam @ApiParam(value = "条码", required = true) String barcode) {
        AccountConsumeSceneDO accountConsumeSceneDO =
                accountService.queryAccountConsumeSceneDO(storeCode, WelfareConstant.ConsumeQueryType.BARCODE, barcode);
        return success(accountConsumeSceneDO);
    }

    @ApiOperation("查询账户信息")
    @GetMapping("/simple")
    public R<AccountDO> queryAccountInfo(@RequestParam @ApiParam(value = "查询条件",required = true) String queryInfo,
                                         @RequestParam @ApiParam("条件类型（barcode:条码,card:磁条信息）") String queryInfoType,
                                         @RequestParam(required = false)  @ApiParam("时间yyyy-MM-dd HH:mm:ss") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date transDate) {
        AccountDO accountDO = accountService.queryByQueryInfo(queryInfo, queryInfoType, transDate);
        return success(accountDO);
    }

    @ApiOperation("账号支付渠道列表")
    @GetMapping("/paymentChannelList")
    @AccountUser
    public R<List<AccountPaymentChannelDTO>> paymentChannelList() {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.queryPaymentChannelList(accountCode));
    }

    @ApiOperation("免密支付签约(页面跳转方式）")
    @GetMapping("/passwordFreePageSign")
    @AccountUser
    public R<AccountPasswordFreePageSignDTO> passwordFreePageSign(String paymentChannel) {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.passwordFreePageSign(accountCode, paymentChannel));
    }

    @ApiOperation("免密签约(APP、小程序或JSAPI）")
    @GetMapping("/passwordFreeSign")
    @AccountUser
    public R<AccountPasswordFreeSignDTO> passwordFreeSign(String paymentChannel) {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.passwordFreeSign(accountCode, paymentChannel));
    }

    @ApiOperation("免密解约(APP、小程序或JSAPI）")
    @GetMapping("/passwordFreeUnsign")
    @AccountUser
    public R<AccountPasswordFreeSignDTO> passwordFreeUnsign(String paymentChannel) {
        Long accountCode = AccountUserHolder.getAccountUser().getAccountCode();
        return success(accountService.passwordFreeUnsign(accountCode, paymentChannel));
    }
}