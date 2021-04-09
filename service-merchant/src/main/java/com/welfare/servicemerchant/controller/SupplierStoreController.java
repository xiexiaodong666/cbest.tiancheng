package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.*;
import com.welfare.servicemerchant.converter.SupplierStoreConverter;
import com.welfare.servicemerchant.dto.SupplierStoreInfo;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 供应商门店服务控制器 Created by hao.yin on 2021/1/7.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/supplier-store")
@Api(tags = "供应商门店相关接口")
public class SupplierStoreController implements IController {

  private final SupplierStoreService supplierStoreService;
  private final SupplierStoreConverter supplierStoreConverter;
  private final FileUploadService fileUploadService;

  @GetMapping("/page")
  @ApiOperation("查询供应商门店列表（分页））")
  public R<Page<SupplierStoreWithMerchantDTO>> page(Page page, StorePageReq req) {
    return R.success(supplierStoreService.page(page, req));
  }

  @GetMapping("/list")
  @ApiOperation("查询供应商门店列表")
  public R<List<SupplierStoreInfo>> list(SupplierStoreListReq req) {
    return R.success(supplierStoreConverter.toD(supplierStoreService.list(req)));
  }

  @GetMapping("/tree")
  @ApiOperation("查询供应商门店列表（树形）")
  public R<List<SupplierStoreTreeDTO>> tree(@RequestParam(required = false) String merCode,
      @RequestParam(required = false) @ApiParam("来源") String source) {
    return R.success(supplierStoreService.tree(merCode, source));
  }

  @GetMapping("/detail")
  @ApiOperation("查询供应商门店详情）")
  public R<SupplierStoreDetailDTO> detail(@ApiParam("id") Long id) {
    return R.success(supplierStoreService.detail(id));
  }

  @PostMapping("/add")
  @ApiOperation("新增供应商门店")
  @ApiUser
  public R add(@RequestBody @Valid SupplierStoreAddDTO supplierStore) {
    if (supplierStore.getConsumType().contains(ConsumeTypeEnum.O2O.getCode())
        && EmptyChecker.isEmpty(supplierStore.getAddressList())) {
      throw new BizException("O2O门店，收货地址必填");
    }
    if (CollectionUtils.isNotEmpty(supplierStore.getStoreConsumeTypeList())
        && supplierStore.getStoreConsumeTypeList().size() > 1) {
      Set<String> set = supplierStore.getStoreConsumeTypeList().stream().map(
          sc -> sc.getCashierNo()).collect(Collectors
                                               .toSet());
      if (set.size() != supplierStore.getStoreConsumeTypeList().size()) {
        throw new BizException("同一门店下虚拟收银机号不能相同");
      }
    }
    return R.status(supplierStoreService.add(supplierStore), "新增失败");
  }

  @PostMapping("/activate")
  @ApiOperation("更改供应商门店激活状态")
  @ApiUser
  public R activate(@RequestBody @Valid SupplierStoreActivateReq storeActivateReq) {
    return R.status(supplierStoreService.activate(storeActivateReq), "更改激活状态失败");
  }

  @PostMapping("/batch-add")
  @ApiOperation("批量新增供应商门店")
  @ApiUser
  public R batchAdd(
      @RequestPart(name = "file") @ApiParam(name = "file", required = true) MultipartFile multipartFile) {
    return R.success(supplierStoreService.upload(multipartFile));
  }

  @PostMapping("/delete/{id}")
  @ApiOperation("删除供应商门店")
  @ApiUser
  public R delete(@PathVariable @NotBlank Long id) {
    return R.status(supplierStoreService.delete(id), "删除失败");
  }

  @PostMapping("/update")
  @ApiOperation("编辑供应商门店")
  @ApiUser
  public R update(@RequestBody @Valid SupplierStoreUpdateDTO supplierStore) {
    if (supplierStore.getConsumType().contains(ConsumeTypeEnum.O2O.getCode())
        && EmptyChecker.isEmpty(supplierStore.getAddressList())) {
      throw new BizException("O2O门店，收货地址必填");
    }

    if (CollectionUtils.isNotEmpty(supplierStore.getStoreConsumeTypeList())
        && supplierStore.getStoreConsumeTypeList().size() > 1) {
      Set<String> set = supplierStore.getStoreConsumeTypeList().stream().map(
          sc -> sc.getCashierNo()).collect(Collectors
                                               .toSet());
      if (set.size() != supplierStore.getStoreConsumeTypeList().size()) {
        throw new BizException("同一门店下虚拟收银机号不能相同");
      }
    }

    return R.status(supplierStoreService.update(supplierStore), "编辑失败失败");
  }

  @PostMapping("/export-list")
  @ApiOperation("导出供应商门店列表")
  public R exportList(@RequestBody(required = false) StorePageReq req) throws IOException {
    return R.success(fileUploadService.getFileServerUrl(fileUploadService.uploadExcelFile(
        supplierStoreService.exportList(req), SupplierStoreWithMerchantDTO.class, "门店导出")));

  }


  @PostMapping("/batch-activate")
  @ApiOperation("更改供应商门店激活状态")
  @ApiUser
  public R batchActivate(@RequestBody List<SupplierStoreActivateReq> storeActivateReqs) {
    supplierStoreService.batchActivate(storeActivateReqs);
    return R.success();
  }
}