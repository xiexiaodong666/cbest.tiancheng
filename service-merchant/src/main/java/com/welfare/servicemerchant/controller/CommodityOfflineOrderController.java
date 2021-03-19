package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderBasicExtResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderBasicResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalResponse;
import com.welfare.service.OrderService;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 5:22 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commodity-offline-order")
@Api(tags = "商品销售查询")
public class CommodityOfflineOrderController {

  private final OrderService orderService;

  private final FileUploadService fileUploadService;

  @GetMapping("/total")
  @ApiOperation("分页查询商品销售线下订单汇总")
  @MerchantUser
  public R<CommodityOfflineOrderBasicResponse<CommodityOfflineOrderTotalResponse>>

  total(@RequestBody CommodityOfflineOrderTotalRequest request) {
    Page page = new Page();
    page.setCurrent(request.getCurrent());
    page.setSize(request.getSize());

    Page<CommodityOfflineOrderTotalResponse> responsePage = orderService
        .getCommodityOfflineOrderTotal(page, request);

    CommodityOfflineOrderBasicResponse<CommodityOfflineOrderTotalResponse> result = new CommodityOfflineOrderBasicResponse<>();

    result.setCurrent(responsePage.getCurrent());
    result.setSize(responsePage.getSize());
    result.setTotal(responsePage.getTotal());
    result.setRecords(responsePage.getRecords());

    if (CollectionUtils.isNotEmpty(result.getRecords())) {
      CommodityOfflineOrderBasicExtResponse ext = orderService.getCommodityOfflineOrderTotalExt(
          request);
      result.setExt(ext);
    }

    return R.success(result);
  }

  @GetMapping("/total/list")
  @ApiOperation("查询商品销售线下订单所有数据")
  @MerchantUser
  public R<CommodityOfflineOrderBasicResponse<CommodityOfflineOrderTotalResponse>> totalExport(@RequestBody CommodityOfflineOrderTotalRequest request) {
    CommodityOfflineOrderBasicResponse<CommodityOfflineOrderTotalResponse> result = new CommodityOfflineOrderBasicResponse<>();
    List<CommodityOfflineOrderTotalResponse> responseList = orderService
        .exportCommodityOfflineOrderTotal(request);
    result.setRecords(responseList);
    if (CollectionUtils.isNotEmpty(result.getRecords())) {
      CommodityOfflineOrderBasicExtResponse ext = orderService.getCommodityOfflineOrderTotalExt(
          request);
      result.setExt(ext);
    }
    return R.success(result);
  }

  @GetMapping("/detail")
  @ApiOperation("分页查询商品销售线下订单明细数据")
  @MerchantUser
  public R<CommodityOfflineOrderBasicResponse<CommodityOfflineOrderDetailResponse>> detail(
      @RequestBody CommodityOfflineOrderDetailRequest request) {

    Page page = new Page();
    page.setCurrent(request.getCurrent());
    page.setSize(request.getSize());

    Page<CommodityOfflineOrderDetailResponse> responsePage = orderService
        .getCommodityOfflineOrderDetail(page, request);

    CommodityOfflineOrderBasicResponse<CommodityOfflineOrderDetailResponse> result = new CommodityOfflineOrderBasicResponse<>();

    result.setCurrent(responsePage.getCurrent());
    result.setSize(responsePage.getSize());
    result.setTotal(responsePage.getTotal());
    result.setRecords(responsePage.getRecords());

    if (CollectionUtils.isNotEmpty(result.getRecords())) {
      CommodityOfflineOrderBasicExtResponse ext = orderService.getCommodityOfflineOrderDetailExt(
          request);
      result.setExt(ext);
    }

    return R.success(result);
  }

  @GetMapping("/detail/list")
  @ApiOperation("查询商品销售线下订单明细所有数据")
  @MerchantUser
  public R<CommodityOfflineOrderBasicResponse<CommodityOfflineOrderDetailResponse>> detailExport(@RequestBody CommodityOfflineOrderDetailRequest request) {
    CommodityOfflineOrderBasicResponse<CommodityOfflineOrderDetailResponse> result = new CommodityOfflineOrderBasicResponse<>();
    List<CommodityOfflineOrderDetailResponse> responseList = orderService
        .exportCommodityOfflineOrderDetail(request);
    result.setRecords(responseList);
    if (CollectionUtils.isNotEmpty(result.getRecords())) {
      CommodityOfflineOrderBasicExtResponse ext = orderService.getCommodityOfflineOrderDetailExt(
          request);
      result.setExt(ext);
    }

    return R.success(result);
  }

}
