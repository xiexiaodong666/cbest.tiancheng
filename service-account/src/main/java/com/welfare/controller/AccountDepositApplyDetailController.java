package com.welfare.controller;

import com.welfare.service.AccountDepositApplyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值申请明细服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 11:08:59
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountDepositApplyDetail")
public class AccountDepositApplyDetailController implements IController {
    private final AccountDepositApplyDetailService accountDepositApplyDetailService;

}