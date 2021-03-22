package com.welfare.service.remote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.offline.OfflineOrderAccountSummaryDTO;
import com.welfare.service.dto.offline.OfflineOrderDTO;
import com.welfare.service.dto.offline.OfflineOrderHangupSummaryDTO;
import com.welfare.service.remote.entity.pos.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 3:25 下午
 */
public interface CbestDmallService {

  /**
   * 分页查询价格模板
   * @param req
   * @return
   */
  Page<PriceTemplateBrief> listPriceTemplate(PosPriceTemplateReq req);

  /**
   * 查询价格模板
   * @param id
   * @return
   */
  PosPriceTemplate queryPriceTemplate(Long id);

  /**
   * 创建价格模板
   * @param req
   * @return
   */
  PosPriceTemplate createPriceTemplate(PosPriceTemplateSaveReq req);

  /**
   * 修改价格模板
   * @param req
   * @return
   */
  PosPriceTemplate modifyPriceTemplate(PosPriceTemplate req);

  /**
   * 分页查询收银机价格模板
   * @param req
   * @return
   */
  Page<PosTerminalPriceTemplateResp> listTerminalPriceTemplate(TerminalPriceTemplateReq req);

  /**
   * 修改收银机价格模板
   * @param req
   * @return
   */
  PosTerminalPriceTemplateResp modifyTerminalPriceTemplate(TerminalPriceTemplateUpdateReq req);

  /**
   * 分页查询离线订单
   * @param req
   * @return
   */
  Page<OfflineOrderDTO> listOfflineTrade(OfflineTradeReq req);

  /**
   * 查询当前挂起的离线订单的汇总数据
   * @param merchantCode
   * @return
   */
  OfflineOrderHangupSummaryDTO summaryHangupOfflineTrade(String merchantCode);

  /**
   * 汇总查询员工的离线订单
   * @param merchantCode
   * @return
   */
  OfflineOrderAccountSummaryDTO summaryAccountOfflineTrade(String merchantCode);

  /**
   * 导出查询离线订单
   * @param req
   * @return
   */
  DmallResponse<Object> exportOfflineTrade(OfflineTradeReq req);
}
