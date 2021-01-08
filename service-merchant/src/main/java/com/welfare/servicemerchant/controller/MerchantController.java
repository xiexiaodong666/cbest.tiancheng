package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.MerchantService;
import com.welfare.service.dto.MerchantPageReq;
import com.welfare.service.dto.MerchantReq;
import com.welfare.servicemerchant.converter.MerchantConverter;
import com.welfare.servicemerchant.dto.MerchantInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * 商户信息服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant")
@Api(tags = "商户相关接口")
public class MerchantController implements IController {
    private final MerchantService merchantService;
    private final MerchantConverter merchantConverter;
    @GetMapping("/list")
    @ApiOperation("查询商户列表（不分页）")
    public R<List<MerchantInfo>> list(@Valid MerchantReq req){
        return R.success(merchantConverter.toD(merchantService.list(req)));
    }
    @GetMapping("/detail")
    @ApiOperation("查询商户详情）")
    public R<MerchantInfo> detail(@RequestParam(required = true) @ApiParam("id") Long id){
        return R.success(merchantConverter.toD(merchantService.detail(id)));
    }

    @GetMapping("/page")
    @ApiOperation("查询商户列表（分页））")
    public R<Page<MerchantInfo>> page(Page<Merchant> page,MerchantPageReq merchantPageReq){
        return R.success(merchantConverter.toD(merchantService.page(page,merchantPageReq)));
    }
    @PostMapping("/add")
    @ApiOperation("新增商户")
    public R add(@RequestBody Merchant merchant){
        return R.status(merchantService.add(merchant),"新增失败");
    }
    @PostMapping("/update")
    @ApiOperation("编辑商户")
    public R update(@RequestBody Merchant merchant){
        return R.status(merchantService.update(merchant),"更新失败");
    }
    @PostMapping("/export-list")
    @ApiOperation("导出商户列表")
    public R exportList(@RequestBody MerchantPageReq merchantPageReq){
        return null;
    }
}