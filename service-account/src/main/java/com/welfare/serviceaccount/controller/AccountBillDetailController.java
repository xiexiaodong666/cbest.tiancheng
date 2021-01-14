package com.welfare.serviceaccount.controller;

import com.welfare.persist.dto.AccountBillDetailSimpleDTO;
import com.welfare.persist.dto.query.AccountBillDetailSimpleReq;
import com.welfare.service.AccountBillDetailService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户流水明细服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 11:08:59
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountBillDetail")
public class AccountBillDetailController implements IController {
    private final AccountBillDetailService accountBillDetailService;

    @ApiOperation("账单列表")
    @GetMapping("/billList/{accountCode}")
    public R<List<AccountBillDetailSimpleDTO>> queryAccountBillDetailSimpleList(
        AccountBillDetailSimpleReq req) {
        return success(accountBillDetailService.queryAccountBillDetailSimpleList(req));
    }
}