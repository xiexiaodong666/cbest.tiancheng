package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.DepartmentService;
import com.welfare.service.converter.DepartmentConverter;
import com.welfare.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public R<List<DepartmentDTO>> list( DepartmentReq req){
        return R.success(departmentConverter.toD(departmentService.list(req)));
    }

    @GetMapping("/tree")
    @ApiOperation("根据商户代码查询商户部门列表（树形）")
    public R<List<DepartmentTree>> tree(String merCode){
        return R.success(departmentService.tree(merCode));
    }

    @GetMapping("/detail")
    @ApiOperation("查询部门详情）")
    public R<DepartmentDTO> detail( @ApiParam("id") Long id){
        return R.success(departmentService.detail(id));
    }

    @PostMapping("/add")
    @ApiOperation("新增部门")
    public R add(@RequestBody@Valid  DepartmentAddDTO department){
        return R.status(departmentService.add(department),"新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("修改部门")
    @ApiUser
    public R update(@RequestBody@Valid  DepartmentUpdateDTO department){
        return R.status(departmentService.update(department),"修改失败");
    }
    @PostMapping("/batch-add")
    @ApiOperation("批量新增部门")
    public R batchAdd(@RequestPart(name = "file") @ApiParam(name = "file", required = true) MultipartFile multipartFile){
        return R.success(departmentService.upload(multipartFile));

    }
    @PostMapping("/delete/{departmentCode}")
    @ApiOperation("删除子部门")
    public R delete(@PathVariable @NotBlank String  departmentCode){
        return R.status(departmentService.delete(departmentCode),"删除失败");
    }

    @GetMapping("/tree-without-merchant")
    @ApiOperation("根据商户代码查询商户部门列表（树形）")
    public R<List<DepartmentTree>> treeWithoutMerchant(String merCode){
        return R.success(departmentService.treeWithoutMerchant(merCode));
    }
}