package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.offline.OfflineOrderAccountSummaryDTO;
import com.welfare.service.dto.offline.OfflineOrderDTO;
import com.welfare.service.dto.offline.OfflineOrderHangupSummaryDTO;
import com.welfare.service.dto.offline.OfflineOrderReq;
import com.welfare.service.remote.service.CbestDmallService;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

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
    return R.success();
  }

  /**
   * 导出离线订单(返回下载地址)
   * @return
   */
  @PostMapping("/export")
  @MerchantUser
  @ApiOperation("导出离线订单(返回下载地址)")
  R<String> offlineOrderExport(@RequestBody OfflineOrderReq req){
    return null;
  }

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @return
   */
  @GetMapping("/hangup/summary")
  @MerchantUser
  @ApiOperation("查询当前挂起的离线订单的汇总数据")
  R<OfflineOrderHangupSummaryDTO> offlineOrderHangupSummary(){
    return null;
  }

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @return
   */
  @GetMapping("/account/summary")
  @MerchantUser
  @ApiOperation("查询当前挂起的离线订单的汇总数据")
  R<OfflineOrderAccountSummaryDTO> offlineOrderAccountSummary(){
    return null;
  }

}
