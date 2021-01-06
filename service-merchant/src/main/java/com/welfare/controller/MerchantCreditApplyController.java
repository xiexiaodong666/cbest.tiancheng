package com.welfare.controller;

import com.welfare.service.MerchantCreditApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商户金额申请服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchantCreditApply")
public class MerchantCreditApplyController implements IController {
    private final MerchantCreditApplyService merchantCreditApplyService;

}