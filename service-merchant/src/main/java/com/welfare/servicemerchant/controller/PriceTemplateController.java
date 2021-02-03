package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.pos.PriceTemplateQueryReq;
import com.welfare.service.dto.pos.TerminalPriceTemplateQueryReq;
import com.welfare.service.remote.entity.pos.*;
import com.welfare.service.remote.service.CbestDmallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 终端相关接口
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 2:49 下午
 */
@Api(tags = "收银机价格模板相关接口")
@RestController
@RequestMapping("/price-template")
@Slf4j
@RequiredArgsConstructor
public class PriceTemplateController implements IController {

  private final CbestDmallService cbestDmallService;

  /**
   * 分页查询价格模板
   * @param req
   * @return
   */
  @PostMapping("/list")
  @MerchantUser
  @ApiOperation("分页查询价格模板")
  R<Page<PriceTemplateBrief>> listPriceTemplate(@RequestBody PriceTemplateQueryReq req){
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(req.getCurrent());
    pagingCondition.setPageSize(req.getSize());
    PosPriceTemplateReq templateReq = PosPriceTemplateReq.builder()
            .name(req.getName())
            .paging(pagingCondition)
            .build();
    if (StringUtils.isNoneBlank(req.getStoreCode())) {
      templateReq.setStoreCodes(Lists.newArrayList(req.getStoreCode()));
    }
    return success(cbestDmallService.listPriceTemplate(templateReq));
  }

  /**
   * 查询价格模板
   * @return
   */
  @GetMapping("/query/{id}")
  @MerchantUser
  @ApiOperation("查询价格模板")
  R<PosPriceTemplate> queryPriceTemplate(@PathVariable("id") Long id){
    return success(cbestDmallService.queryPriceTemplate(id));
  }

  /**
   * 创建价格模板
   * @param req
   * @return
   */
  @PostMapping("/create")
  @MerchantUser
  @ApiOperation("创建价格模板")
  R<PosPriceTemplate> createPriceTemplate(@RequestBody PosPriceTemplateSaveReq req){
    return success(cbestDmallService.createPriceTemplate(req));
  }

  /**
   * 修改价格模板
   * @param req
   * @return
   */
  @PostMapping("/modify")
  @MerchantUser
  @ApiOperation("修改价格模板")
  R<PosPriceTemplate> modifyPriceTemplate(@RequestBody PosPriceTemplate req){
    return success(cbestDmallService.modifyPriceTemplate(req));
  }

  /**
   * 分页查询收银机价格模板
   * @param req
   * @return
   */
  @PostMapping("terminal/list")
  @MerchantUser
  @ApiOperation("分页查询收银机价格模板")
  R<Page<PosTerminalPriceTemplateResp>> listTerminalPriceTemplate(@RequestBody TerminalPriceTemplateQueryReq req){
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(req.getCurrent());
    pagingCondition.setPageSize(req.getSize());
    TerminalPriceTemplateReq posPriceTemplateReq = TerminalPriceTemplateReq.builder()
            .paging(pagingCondition)
            .build();
    if (StringUtils.isNoneBlank(req.getStoreCode())) {
      posPriceTemplateReq.setStoreCodes(Lists.newArrayList(req.getStoreCode()));
    }
    return success(cbestDmallService.listTerminalPriceTemplate(posPriceTemplateReq));
  }

  /**
   * 修改收银机价格模板
   * @param req
   * @return
   */
  @PostMapping("/terminal/modify")
  @MerchantUser
  @ApiOperation("修改收银机价格模板")
  R<PosTerminalPriceTemplateResp> modifyTerminalPriceTemplate(@RequestBody TerminalPriceTemplateUpdateReq req) {
    return success(cbestDmallService.modifyTerminalPriceTemplate(req));
  }

  /**
   * 查询收银机价格模板
   * @param storeCode
   * @return
   */
  @PostMapping("/all/{storeCode}")
  @MerchantUser
  @ApiOperation("查询价格模板")
  R<List<PriceTemplateBrief>> allTerminalPriceTemplate(@PathVariable("storeCode") String storeCode){
    PriceTemplateQueryReq req = new PriceTemplateQueryReq();
    req.setStoreCode(storeCode);
    req.setCurrent(1);
    req.setSize(999);
    R<Page<PriceTemplateBrief>> r = listPriceTemplate(req);
    if (r.isSuccess()) {
      return success(r.getData() != null ? r.getData().getRecords() : null);
    } else {
      return fail(r.getMsg());
    }
  }
}
