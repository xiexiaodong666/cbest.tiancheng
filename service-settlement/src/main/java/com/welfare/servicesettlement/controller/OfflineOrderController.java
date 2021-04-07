package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.service.dto.offline.*;
import com.welfare.service.remote.entity.pos.OfflineTradeReq;
import com.welfare.service.remote.entity.pos.PagingCondition;
import com.welfare.service.remote.entity.pos.PosPriceTemplateReq;
import com.welfare.service.remote.service.CbestDmallService;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 2:32 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/offline/order")
@Api(tags = "离线订单管理")
public class OfflineOrderController {

  private final CbestDmallService cbestDmallService;
  private final FileUploadServiceUtil fileUploadServiceUtil;
  /**
   * 分页查询离线订单
   * @param req
   * @return
   */
  @PostMapping("/list")
  @MerchantUser
  @ApiOperation("分页查询离线订单")
  R<Page<OfflineOrderDTO>> offlineOrderList(@RequestBody OfflineOrderReq req){
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(req.getCurrent());
    pagingCondition.setPageSize(req.getSize());
    OfflineTradeReq offlineTradeReq = new OfflineTradeReq();
    BeanUtils.copyProperties(req, offlineTradeReq);
    offlineTradeReq.setPaging(pagingCondition);
    offlineTradeReq.setMerchantCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    return R.success(cbestDmallService.listOfflineTrade(offlineTradeReq));
  }

  /**
   * 导出离线订单(返回下载地址)
   * @return
   */
  @PostMapping("/export")
  @MerchantUser
  @ApiOperation("导出离线订单(返回下载地址)")
  R<String> offlineOrderExport(@RequestBody OfflineOrderExportReq req) throws IOException {
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(1);
    pagingCondition.setPageSize(2000);
    OfflineTradeReq offlineTradeReq = new OfflineTradeReq();
    BeanUtils.copyProperties(req, offlineTradeReq);
    offlineTradeReq.setPaging(pagingCondition);
    offlineTradeReq.setMerchantCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    Page<OfflineOrderDTO> dtoPage = cbestDmallService.listOfflineTrade(offlineTradeReq);
    List<OfflineOrderDTO> list = new ArrayList<>();
    if (dtoPage != null && CollectionUtils.isNotEmpty(dtoPage.getRecords())) {
      list = dtoPage.getRecords();
    }
    List<OfflineOrderDTO.OfflineOrderDTO2> offlineOrderDTO2s = new ArrayList<>();
    list.forEach(offlineOrderDTO -> {
      OfflineOrderDTO.OfflineOrderDTO2 dto2 = new OfflineOrderDTO.OfflineOrderDTO2();
      BeanUtils.copyProperties(offlineOrderDTO, dto2, "amount");
      dto2.setAmount(changeF2Y(offlineOrderDTO.getAmount()));
      offlineOrderDTO2s.add(dto2);
    });
    String path = fileUploadServiceUtil.uploadExcelFile(offlineOrderDTO2s, OfflineOrderDTO.OfflineOrderDTO2.class, "离线订单");
    return R.success(fileUploadServiceUtil.getFileServerUrl(path));
  }

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @return
   */
  @GetMapping("/hangup/summary")
  @MerchantUser
  @ApiOperation("查询当前挂起的离线订单的汇总数据")
  R<OfflineOrderHangupSummaryDTO> offlineOrderHangupSummary(){
    return R.success(cbestDmallService.summaryHangupOfflineTrade(MerchantUserHolder.getMerchantUser().getMerchantCode()));
  }

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @return
   */
  @GetMapping("/account/summary")
  @MerchantUser
  @ApiOperation("查询当前挂起的离线订单的汇总数据")
  R<List<OfflineOrderAccountSummaryDTO>> offlineOrderAccountSummary(){
    return R.success(cbestDmallService.summaryAccountOfflineTrade(MerchantUserHolder.getMerchantUser().getMerchantCode()));
  }
  /**
   * 分转元，转换为bigDecimal在toString
   * @return
   */
  private BigDecimal changeF2Y(Integer price) {
    if (price == null) {
      return BigDecimal.ZERO;
    }
    return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
  }
}
