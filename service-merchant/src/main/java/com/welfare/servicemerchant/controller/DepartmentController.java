package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.entity.Department;
import com.welfare.service.DepartmentService;
import com.welfare.service.dto.DepartmentReq;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.servicemerchant.converter.DepartmentConverter;
import com.welfare.servicemerchant.dto.DepartmentInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 商户部门服务控制器
 * Created by hao.yin on 2021/1/7.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/department")
@Api(tags = "商户部门相关接口")
public class DepartmentController implements IController {
    private final DepartmentService departmentService;
    private final DepartmentConverter departmentConverter;
    @GetMapping("/list")
    @ApiOperation("根据商户代码查询商户部门列表（不分页）")
    @MerchantUser
    public R<List<DepartmentInfo>> list(@Valid DepartmentReq req){
        return R.success(departmentConverter.toD(departmentService.list(req)));
    }

    @GetMapping("/tree")
    @ApiOperation("根据商户代码查询商户部门列表（树形）")
    public R<List<DepartmentTree>> tree(String merCode){
        return R.success(departmentService.tree(merCode));
    }

    @GetMapping("/detail")
    @ApiOperation("查询商户详情）")
    public R<DepartmentInfo> detail(@RequestParam(required = true) @ApiParam("id") Long id){
        return R.success(departmentConverter.toD(departmentService.detail(id)));
    }

    @PostMapping("/add")
    @ApiOperation("新增商户")
    public R add(@RequestBody Department department){
        return R.status(departmentService.add(department),"新增失败");
    }
    @PostMapping("/batch-add")
    @ApiOperation("批量新增子机构")
    public R batchAdd(@RequestBody List<Department> list){
        return R.status(departmentService.batchAdd(list),"批量新增失败");

    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除子机构")
    public R delete(@PathVariable @NotBlank String  departmentCode){
        return R.status(departmentService.delete(departmentCode),"删除失败");
    }
}