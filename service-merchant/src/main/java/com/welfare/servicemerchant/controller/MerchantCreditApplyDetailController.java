package com.welfare.servicemerchant.controller;

import com.welfare.service.MerchantCreditApplyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户明细记录服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchantCreditApplyDetail")
public class MerchantCreditApplyDetailController implements IController {
    private final MerchantCreditApplyDetailService merchantCreditApplyDetailService;

}