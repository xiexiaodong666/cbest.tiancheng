package com.welfare.servicemerchant.controller;

import com.welfare.service.MerchantService;
import com.welfare.servicemerchant.dto.MerchantInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/list")
    @ApiOperation("查询商户列表（不分页）")
    public R<List<MerchantInfo>> list(@RequestParam @ApiParam("商户类型") String merType,
                                      @RequestParam @ApiParam("身份属性") String merIdentity,
                                      @RequestParam @ApiParam("合作方式") String merCooperationMode){
        return null;
    }
}