package com.welfare.service.remote.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.offline.OfflineOrderAccountSummaryDTO;
import com.welfare.service.dto.offline.OfflineOrderDTO;
import com.welfare.service.dto.offline.OfflineOrderHangupSummaryDTO;
import com.welfare.service.remote.CbestDmallFeign;
import com.welfare.service.remote.entity.pos.*;
import com.welfare.service.remote.service.CbestDmallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 3:26 下午
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CbestDmallServiceImpl implements CbestDmallService {
  @Autowired(required = false)
  private CbestDmallFeign cbestDmallFeign;
  private final SupplierStoreDao supplierStoreDao;

  @Override
  public Page<PriceTemplateBrief> listPriceTemplate(PosPriceTemplateReq req) {
    if (CollectionUtils.isEmpty(req.getStoreCodes())) {
      String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
      List<SupplierStore> supplierStores =  supplierStoreDao
              .listByMerCode(merCode);
      if (CollectionUtils.isEmpty(supplierStores)) {
        log.warn("商户下没有线下门店直接返回 请求:{} 商户:{}", JSON.toJSONString(req), merCode);
        return toPage(null, req.getPaging());
      }
      req.setStoreCodes(supplierStores.stream().map(SupplierStore::getStoreCode).collect(Collectors.toList()));
    }
    DmallResponse<PagingResult<PriceTemplateBrief>> resp = cbestDmallFeign.listPriceTemplate(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(resp.getCode())) {
      log.error("分页查询价格模板失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(resp));
      throw new BizException(resp.getMsg());
    }
    return toPage(resp.getData(), req.getPaging());
  }

  @Override
  public PosPriceTemplate queryPriceTemplate(Long id) {
    Map<String, Long> parma = new HashMap<>();
    parma.put("id", id);
    DmallResponse<PosPriceTemplate> response = cbestDmallFeign.queryPriceTemplate(parma);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(response.getCode())) {
      log.error("查询价格模板失败 请求:{} 响应:{}", id, JSON.toJSONString(response));
      throw new BizException(response.getMsg());
    }
    return response.getData();
  }

  @Override
  public PosPriceTemplate createPriceTemplate(PosPriceTemplateSaveReq req) {
    DmallResponse<PosPriceTemplate> response = cbestDmallFeign.createPriceTemplate(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(response.getCode())) {
      log.error("创建价格模板失败失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(response));
      throw new BizException(response.getMsg());
    }
    return response.getData();
  }

  @Override
  public PosPriceTemplate modifyPriceTemplate(PosPriceTemplate req) {
    DmallResponse<PosPriceTemplate> response = cbestDmallFeign.modifyPriceTemplate(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(response.getCode())) {
      log.error("修改价格模板失败失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(response));
      throw new BizException(response.getMsg());
    }
    return response.getData();
  }

  @Override
  public Page<PosTerminalPriceTemplateResp> listTerminalPriceTemplate(TerminalPriceTemplateReq req) {
    if (CollectionUtils.isEmpty(req.getStoreCodes())) {
      String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
      List<SupplierStore> supplierStores = supplierStoreDao
              .listByMerCode(merCode);
      if (CollectionUtils.isEmpty(supplierStores)) {
        log.warn("商户下没有线下门店直接返回 请求:{} 商户:{}", JSON.toJSONString(req), merCode);
        return toPage(null, req.getPaging());
      }
      req.setStoreCodes(supplierStores.stream().map(SupplierStore::getStoreCode).collect(Collectors.toList()));
    }
    DmallResponse<PagingResult<PosTerminalPriceTemplateResp>> resp = cbestDmallFeign.listTerminalPriceTemplate(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(resp.getCode())) {
      log.error("分页查询收银机价格模板失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(resp));
      throw new BizException(resp.getMsg());
    }
    return toPage(resp.getData(), req.getPaging());
  }

  @Override
  public PosTerminalPriceTemplateResp modifyTerminalPriceTemplate(TerminalPriceTemplateUpdateReq req) {
    DmallResponse<PosTerminalPriceTemplateResp> response = cbestDmallFeign.modifyTerminalPriceTemplate(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(response.getCode())) {
      log.error("修改收银机价格模板失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(response));
      throw new BizException(response.getMsg());
    }
    return response.getData();
  }

  @Override
  public Page<OfflineOrderDTO> listOfflineTrade(OfflineTradeReq req) {
    DmallResponse<PagingResult<OfflineOrderDTO>> resp = cbestDmallFeign.listOfflineTrade(req);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(resp.getCode())) {
      log.error("分页查询离线订单失败 请求:{} 响应:{}", JSON.toJSONString(req), JSON.toJSONString(resp));
      throw new BizException(resp.getMsg());
    }
    return toPage(resp.getData(), req.getPaging());
  }

  @Override
  public OfflineOrderHangupSummaryDTO summaryHangupOfflineTrade(String merchantCode) {
    DmallResponse<OfflineOrderHangupSummaryDTO> resp = cbestDmallFeign.summaryHangupOfflineTrade(merchantCode);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(resp.getCode())) {
      log.error("查询当前挂起的离线订单的汇总数据失败 请求:{} 响应:{}", merchantCode, JSON.toJSONString(resp));
      throw new BizException(resp.getMsg());
    }
    return resp.getData();
  }

  @Override
  public OfflineOrderAccountSummaryDTO summaryAccountOfflineTrade(String merchantCode) {
    DmallResponse<OfflineOrderAccountSummaryDTO> resp = cbestDmallFeign.summaryAccountOfflineTrade(merchantCode);
    if (!CbestDmallFeign.SUCCESS_CODE.equals(resp.getCode())) {
      log.error("汇总查询员工的离线订单失败 请求:{} 响应:{}", merchantCode, JSON.toJSONString(resp));
      throw new BizException(resp.getMsg());
    }
    return resp.getData();
  }

  private <T> Page<T> toPage(PagingResult<T> pagingResult, PagingCondition pagingCondition) {
    Page<T> page = new Page<>();
    page.setCurrent(pagingCondition.getPageNo());
    page.setSize(pagingCondition.getPageSize());
    if (Objects.nonNull(pagingResult)) {
      page.setRecords(pagingResult.getList());
      page.setTotal(pagingResult.getTotal());
      page.setPages(pagingResult.getTotal() % pagingResult.getPageSize() != 0 ?
              pagingResult.getTotal() % pagingResult.getPageSize() :
              pagingResult.getTotal() % pagingResult.getPageSize() + 1);
    }
    return page;
  }
}
