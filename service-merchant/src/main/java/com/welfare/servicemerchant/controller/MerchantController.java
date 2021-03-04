package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.service.MerchantService;
import com.welfare.service.dto.*;
import com.welfare.servicemerchant.converter.MerchantConverter;
import com.welfare.servicemerchant.dto.MerchantInfo;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 商户信息服务控制器
 *
 * @author hao.yin
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant")
@Api(tags = "商户相关接口")
public class MerchantController implements IController {
    private final MerchantService merchantService;
    private final MerchantConverter merchantConverter;
    private final FileUploadService fileUploadService;

    @GetMapping("/list")
    @ApiOperation("查询商户列表（不分页）")
    public R<List<MerchantInfo>> list(@Valid MerchantReq req) {
        return R.success(merchantConverter.toD(merchantService.list(req)));
    }

    @GetMapping("/detail")
    @ApiOperation("查询商户详情）")
    public R<MerchantDetailDTO> detail(@RequestParam(required = true) @ApiParam("id") Long id) {
        return R.success(merchantService.detail(id));
    }

    @GetMapping("/tree")
    @ApiOperation("查询商户列表树形）")
    public R<List<MerchantWithCreditAndTreeDTO>> tree(MerchantPageReq merchantPageReq) {
        return R.success(merchantService.tree(merchantPageReq));
    }

    @PostMapping("/add")
    @ApiOperation("新增商户")
    public R add(@RequestBody @Valid MerchantAddDTO merchant) {
        if (MerIdentityEnum.customer.getCode().equals(merchant.getMerType())
                && EmptyChecker.isEmpty(merchant.getAddressList())) {
            throw new BusiException("身份属性为客户时，收获地址必填");
        }
        return R.status(merchantService.add(merchant), "新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("编辑商户")
    @ApiUser
    public R update(@RequestBody @Valid MerchantUpdateDTO merchant) {
        if (MerIdentityEnum.customer.getCode().equals(merchant.getMerType())
                && EmptyChecker.isEmpty(merchant.getAddressList())) {
            throw new BusiException("身份属性为客户时，收货地址必填");
        }
        return R.status(merchantService.update(merchant), "更新失败");
    }

    @PostMapping("/export-list")
    @ApiOperation("导出商户列表")
    public R<String> exportList(@RequestBody(required = false) MerchantPageReq merchantPageReq) throws IOException {
        return R.success(fileUploadService.getFileServerUrl(fileUploadService.uploadExcelFile(merchantService.exportList(merchantPageReq), MerchantWithCreditAndTreeDTO.class, "商户导出")));
    }


    @GetMapping("/hasRebate")
    @ApiOperation("查询商户是否有配置返利")
    @MerchantUser
    public R<Boolean> hasRebateByMerCode(){
        return success(merchantService.queryIsRabteByMerCOde(MerchantUserHolder.getMerchantUser().getMerchantCode()));
    }
}