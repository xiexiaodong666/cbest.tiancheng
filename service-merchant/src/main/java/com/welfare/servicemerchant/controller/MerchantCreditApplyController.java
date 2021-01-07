package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.MerchantCreditApplyService;
import com.welfare.servicemerchant.dto.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/merchant-credit-apply")
public class MerchantCreditApplyController implements IController {
    private final MerchantCreditApplyService merchantCreditApplyService;



    @GetMapping("/page")
    @ApiOperation("分页商户额度申请列表")
    public R<Page<AccountDepositApplyInfo>> page(@RequestParam @ApiParam("当前页") Integer currentPage,
                                                 @RequestParam @ApiParam("单页大小") Integer pageSize,
                                                 AccountDepositApplyQuery query){
        return null;
    }

    @GetMapping("/detail")
    @ApiOperation("查询商户额度申请详情")
    public R<MerchantCreditApplyInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Integer id){
        return null;
    }

    @PostMapping("/update")
    @ApiOperation("修改商户额度申请")
    public R<Long> update(@RequestBody MerchantCreditApplyRequest requst){
        return null;
    }

    @PostMapping("/export")
    @ApiOperation("导出商户额度申请(返回文件下载地址)")
    public R<String> export(@RequestBody AccountDepositApplyQuery query){
        return null;
    }

    @PostMapping("/approval")
    @ApiOperation("审批商户额度申请")
    public R<Long> approval(@RequestBody AccountDepositApprovalRequest request){
        return null;
    }

    @PostMapping("/save")
    @ApiOperation("新增商户额度申请")
    public R<Long> save(@RequestBody BatchDepositApplyRequest request){
        return null;
    }
}