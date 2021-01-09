package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.SupplierStorePageReq;
import com.welfare.servicemerchant.converter.SupplierStoreConverter;
import com.welfare.servicemerchant.dto.SupplierStoreInfo;
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

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 供应商门店服务控制器
 * Created by hao.yin on 2021/1/7.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/supplier-store")
@Api(tags = "供应商门店相关接口")
public class SupplierStoreController implements IController {
    private final SupplierStoreService supplierStoreService;
    private final SupplierStoreConverter supplierStoreConverter;
    @GetMapping("/page")
    @ApiOperation("查询供应商门店列表（分页））")
    public R<Page<SupplierStoreInfo>> page(Page<SupplierStore>page, SupplierStorePageReq req){
        return R.success(supplierStoreConverter.toD(supplierStoreService.page(page,req)));
    }
    @GetMapping("/detail")
    @ApiOperation("查询供应商门店详情）")
    public R<SupplierStoreInfo> detail(@RequestParam(required = true) @ApiParam("id") Long id){
        return R.success(supplierStoreConverter.toD(supplierStoreService.detail(id)));
    }

    @PostMapping("/add")
    @ApiOperation("新增供应商门店")
    public R<SupplierStoreInfo> add(@RequestBody SupplierStore supplierStore){
        return R.status(supplierStoreService.add(supplierStore),"新增失败");
    }

    @PostMapping("/activate/{id}/{status}")
    @ApiOperation("更改供应商门店激活状态")
    public R activate(@PathVariable @NotBlank Long id, @PathVariable@NotBlank Integer status){
        return R.status(supplierStoreService.activate(id,status),"更改激活状态失败");
    }
    @PostMapping("/batch-add")
    @ApiOperation("批量新增供应商门店")
    public R batchAdd(@RequestBody List<SupplierStore> list){
        return R.status(supplierStoreService.batchAdd(list),"批量新增失败");
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除供应商门店")
    public R<SupplierStoreInfo> delete(@PathVariable @NotBlank Long id){
        return R.status(supplierStoreService.delete(id),"删除失败");
    }
    @PostMapping("/update")
    @ApiOperation("编辑供应商门店")
    public R update(@RequestBody SupplierStore supplierStore){
        // TODO green.gao 消费能力变更后,同步修改到消费门店的消费能力字段。
        return R.status(supplierStoreService.add(supplierStore),"编辑失败失败");

    }
    @PostMapping("/export-list")
    @ApiOperation("导出供应商门店列表")
    public R exportList(SupplierStorePageReq req){
        return R.success(supplierStoreService.exportList(req));

    }
}