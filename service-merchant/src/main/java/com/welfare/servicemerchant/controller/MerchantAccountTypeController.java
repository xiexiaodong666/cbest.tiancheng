package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.dto.MerchantAccountTypePageReq;
import com.welfare.service.dto.MerchantAccountTypeReq;
import com.welfare.service.dto.MerchantPageReq;
import com.welfare.servicemerchant.converter.MerchantAccountTypeConverter;
import com.welfare.servicemerchant.dto.MerchantAccountTypeInfo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 商户福利类型服务控制器
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant")
@Api(tags = "商户相关接口")
public class MerchantAccountTypeController implements IController {
    private final MerchantAccountTypeService merchantAccountTypeService;
    private final MerchantAccountTypeConverter merchantAccountTypeConverter;
    @GetMapping("/list")
    @ApiOperation("查询商户列表（不分页）")
    public R<List<MerchantAccountTypeInfo>> list(@Valid MerchantAccountTypeReq req){
        return R.success(merchantAccountTypeConverter.toD(merchantAccountTypeService.list(req)));
    }
    @GetMapping("/detail")
    @ApiOperation("查询商户详情）")
    public R<MerchantAccountTypeInfo> detail(@RequestParam(required = true) @ApiParam("id") Long id){
        return R.success(merchantAccountTypeConverter.toD(merchantAccountTypeService.detail(id)));
    }

    @GetMapping("/page")
    @ApiOperation("查询商户列表（分页））")
    public R<Page<MerchantAccountTypeInfo>> page(Page page, MerchantAccountTypePageReq pageReq){
        return R.success(merchantAccountTypeConverter.toD(merchantAccountTypeService.page(page,pageReq)));
    }
    @PostMapping("/add")
    @ApiOperation("新增商户")
    public R add(@RequestBody MerchantAccountType merchantAccountType){
        return R.status(merchantAccountTypeService.add(merchantAccountType),"新增失败");
    }
    @PostMapping("/update")
    @ApiOperation("编辑商户")
    public R update(@RequestBody MerchantAccountType merchantAccountType){
        return R.status(merchantAccountTypeService.update(merchantAccountType),"更新失败");
    }
    @PostMapping("/export-list")
    @ApiOperation("导出商户列表")
    public R exportList(@RequestBody MerchantAccountTypePageReq pageReq){
        return null;
    }

    @PostMapping("/move-deductions-order")
    @ApiOperation("移动商户福利类型扣款顺序")
    public R moveDeductionsOrder(@RequestBody MerchantPageReq merchantPageReq){
        return R.status(merchantAccountTypeService.moveDeductionsOrder(),"移动扣款顺序失败");
    }
}