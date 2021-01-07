package com.welfare.servicemerchant.controller;

import com.welfare.service.MerchantService;
import com.welfare.servicemerchant.dto.DepartmentInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户部门服务控制器
 * Created by hao.yin on 2021/1/7.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/department")
public class DepartmentController implements IController {
    private final MerchantService merchantService;
    @GetMapping("/list")
    @ApiOperation("根据商户代码查询商户部门列表（不分页）")
    public R<List<DepartmentInfo>> list(@RequestParam(required = true) @ApiParam("商户代码") String merCode,
                                        @RequestParam @ApiParam("部门父级") String departmentParent,
                                        @RequestParam @ApiParam("部门层级") String departmentLevel){
        return null;
    }
}