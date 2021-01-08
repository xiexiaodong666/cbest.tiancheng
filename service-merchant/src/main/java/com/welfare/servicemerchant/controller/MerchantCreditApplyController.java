package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.MerchantCreditApplyService;
import com.welfare.servicemerchant.dto.*;
import io.swagger.annotations.Api;
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
@Api(tags = "商户额度申请管理")
public class MerchantCreditApplyController implements IController {
    private final MerchantCreditApplyService merchantCreditApplyService;


    @GetMapping("/page")
    @ApiOperation("分页商户额度申请列表")
    @MerchantUser
    public R<Page<MerchantCreditApplyInfo>> page(@RequestParam @ApiParam("当前页") Integer currentPage,
                                                 @RequestParam @ApiParam("单页大小") Integer pageSize,
                                                 MerchantCreditApplyQuery query){
        return null;
    }

    @GetMapping("/detail")
    @ApiOperation("查询商户额度申请详情")
    @MerchantUser
    public R<MerchantCreditApplyInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Integer id){
        return null;
    }

    @PostMapping("/update")
    @ApiOperation("修改商户额度申请")
    @MerchantUser
    public R<Long> update(@RequestBody MerchantCreditApplyUpdateReq request){
        return null;
    }

    @PostMapping("/export")
    @ApiOperation("导出商户额度申请(返回文件下载地址)")
    @MerchantUser
    public R<String> export(@RequestBody MerchantCreditApplyQuery query){
        return null;
    }

    @PostMapping("/approval")
    @ApiOperation("审批商户额度申请")
    @MerchantUser
    public R<Long> approval(@RequestBody MerchantCreditApprovalReq request){
        return null;
    }

    @PostMapping("/save")
    @ApiOperation("新增商户额度申请")
    @MerchantUser
    public R<Long> save(@RequestBody MerchantCreditApplyRequest request){
        return null;
    }
}