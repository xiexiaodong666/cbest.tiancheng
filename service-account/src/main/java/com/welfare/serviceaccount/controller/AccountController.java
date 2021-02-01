package com.welfare.serviceaccount.controller;

import com.welfare.common.annotation.AccountUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.AccountUserHolder;
import com.welfare.persist.dto.AccountConsumeSceneDO;
import com.welfare.persist.dto.AccountSimpleDTO;
import com.welfare.service.AccountService;
import io.swagger.annotations.Api;
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

    @ApiOperation("查询账户消费场景-卡内信息")
    @GetMapping("/consume-scene/card")
    public R<AccountConsumeSceneDO> queryAccountConsumeSceneDOByCardInfo(@RequestParam @ApiParam(value = "门店号",required = true) String storeCode,
                                                               @RequestParam @ApiParam(value = "卡内信息",required = true) String cardInsideInfo){
        AccountConsumeSceneDO accountConsumeSceneDO =
                accountService.queryAccountConsumeSceneDO(storeCode, WelfareConstant.ConsumeQueryType.CARD, cardInsideInfo);
        return success(accountConsumeSceneDO);
    }

    @ApiOperation("查询账户消费场景-条码")
    @GetMapping("/consume-scene/barcode")
    public R<AccountConsumeSceneDO> queryAccountConsumeSceneDOByBarcode(@RequestParam @ApiParam(value = "门店号",required = true) String storeCode,
                                                               @RequestParam @ApiParam(value = "条码",required = true) String barcode){
        AccountConsumeSceneDO accountConsumeSceneDO =
                accountService.queryAccountConsumeSceneDO(storeCode, WelfareConstant.ConsumeQueryType.BARCODE, barcode);
        return success(accountConsumeSceneDO);
    }
}