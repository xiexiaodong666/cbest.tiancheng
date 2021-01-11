package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.util.ApiUserHolder;
import com.welfare.service.MerchantCreditApplyService;
import com.welfare.service.dto.*;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @Autowired
    private MerchantCreditApplyService applyService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/page")
    @ApiOperation("分页商户额度申请列表")
    @ApiUser
    public R<Page<MerchantCreditApplyInfo>> page(@RequestParam @ApiParam("当前页（从1开始）") Integer current,
                                                 @RequestParam @ApiParam("单页大小") Integer size,
                                                 @Validated MerchantCreditApplyQuery query){
        return success(applyService.page(current, size, query, ApiUserHolder.getUserInfo()));
    }

    @GetMapping("/detail")
    @ApiOperation("查询商户额度申请详情")
    @ApiUser
    public R<MerchantCreditApplyInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Long id){
        return success(applyService.detail(id));
    }

    @PostMapping("/update")
    @ApiOperation("修改商户额度申请")
    @ApiUser
    public R<Long> update(@Validated @RequestBody MerchantCreditApplyUpdateReq request){
        return success(applyService.update(request, ApiUserHolder.getUserInfo()));
    }

    @PostMapping("/export")
    @ApiOperation("导出商户额度申请(返回文件下载地址)")
    @ApiUser
    public R<String> export(@Validated @RequestBody MerchantCreditApplyQuery query) throws IOException {
        List<MerchantCreditApplyInfo> list = applyService.list(query, ApiUserHolder.getUserInfo());
        return success(fileUploadService.uploadExcelFile(list, MerchantCreditApplyInfo.class, "商户额度申请"));
    }

    @PostMapping("/approval")
    @ApiOperation("审批商户额度申请")
    @ApiUser
    public R<Long> approval(@Validated @RequestBody MerchantCreditApprovalReq request){

        return success(applyService.approval(request, ApiUserHolder.getUserInfo()));
    }

    @PostMapping("/save")
    @ApiOperation("新增商户额度申请")
    @ApiUser
    public R<Long> save(@Validated @RequestBody MerchantCreditApplyRequest request){
        return success(applyService.save(request, ApiUserHolder.getUserInfo()));
    }
}