package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.dto.messagepushconfig.WarningSettingSaveReq;
import com.welfare.service.dto.offline.OfflineOrderAccountSummaryDTO;
import com.welfare.service.dto.offline.OfflineOrderDTO;
import com.welfare.service.dto.offline.OfflineOrderExportReq;
import com.welfare.service.dto.offline.OfflineOrderHangupSummaryDTO;
import com.welfare.service.remote.config.FeignConfiguration;
import com.welfare.service.remote.entity.pos.*;
import com.welfare.service.remote.fallback.CbestDmallFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 机具相关远程服务
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:36 上午
 */
@FeignClient(value = "CbestDmall-Interface", url = "${cbest.dmall.url:http://dmall.e-cbest.com}", fallbackFactory = CbestDmallFeignFallback.class, configuration = FeignConfiguration.class)
@ConditionalOnHavingProperty("cbest.dmall.url")
public interface CbestDmallFeign {

  String SUCCESS_CODE = "0000";

  /**
   * 分页查询价格模板
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/price-template/list")
  DmallResponse<PagingResult<PriceTemplateBrief>> listPriceTemplate(@RequestBody PosPriceTemplateReq req);

  /**
   * 查询价格模板
   * @param id
   * @return
   */
  @PostMapping("/orangeapi/manage/price-template/query")
  DmallResponse<PosPriceTemplate> queryPriceTemplate(@NotNull(message = "id不能为空") Map<String, Long> id);

  /**
   * 创建价格模板
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/price-template/create")
  DmallResponse<PosPriceTemplate> createPriceTemplate(@RequestBody PosPriceTemplateSaveReq req);

  /**
   * 修改价格模板
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/price-template/modify")
  DmallResponse<PosPriceTemplate> modifyPriceTemplate(@RequestBody PosPriceTemplate req);

  /**
   * 分页查询收银机价格模板
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/terminal/price-template/list")
  DmallResponse<PagingResult<PosTerminalPriceTemplateResp>> listTerminalPriceTemplate(@RequestBody TerminalPriceTemplateReq req);

  /**
   * 修改收银机价格模板
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/terminal/price-template/modify")
  DmallResponse<PosTerminalPriceTemplateResp> modifyTerminalPriceTemplate(@RequestBody TerminalPriceTemplateUpdateReq req);

  /**
   * 分页查询离线订单
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/offline-trade/list")
  DmallResponse<PagingResult<OfflineOrderDTO>> listOfflineTrade(@RequestBody OfflineTradeReq req);

  /**
   * 导出查询离线订单
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/offline-trade/download")
  DmallResponse<Object> exportOfflineTrade(@RequestBody OfflineTradeReq req);

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @param merchantCode
   * @return
   */
  @PostMapping("/orangeapi/manage/offline-trade/hangup/summary")
  DmallResponse<OfflineOrderHangupSummaryDTO> summaryHangupOfflineTrade(@RequestParam("merchantCode") String merchantCode);

  /**
   * 汇总查询员工的离线订单
   * @param merchantCode
   * @return
   */
  @PostMapping("/orangeapi/manage/offline-trade/account/summary")
  DmallResponse<OfflineOrderAccountSummaryDTO> summaryAccountOfflineTrade(@RequestParam("merchantCode") String merchantCode);

  /**
   * 保存短信通知设置
   * @param req
   * @return
   */
  @PostMapping("/orangeapi/manage/offline-trade/warning-setting/save")
  DmallResponse<Object> saveWarningSetting(@RequestBody WarningSettingSaveReq req);
}
