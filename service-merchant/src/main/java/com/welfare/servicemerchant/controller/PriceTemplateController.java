package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.pos.PriceTemplateQueryReq;
import com.welfare.service.dto.pos.TerminalPriceTemplateQueryReq;
import com.welfare.service.remote.entity.pos.*;
import com.welfare.service.remote.service.CbestDmallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
  R<Page<PriceTemplateBrief>> listPriceTemplate(@RequestBody PriceTemplateQueryReq req){
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(req.getCurrent());
    pagingCondition.setPageSize(req.getSize());
    PosPriceTemplateReq templateReq = PosPriceTemplateReq.builder()
            .name(req.getName())
            .storeCodes(Lists.newArrayList(req.getStoreCode()))
            .paging(pagingCondition)
            .build();
    return success(cbestDmallService.listPriceTemplate(templateReq));
  }

  /**
   * 查询价格模板
   * @param id
   * @return
   */
  @GetMapping("/query")
  @MerchantUser
  R<PosPriceTemplate> queryPriceTemplate(@ApiParam(name = "id", required = true)
                                         @NotNull(message = "id不能为空") Long id){
    return success(cbestDmallService.queryPriceTemplate(id));
  }

  /**
   * 创建价格模板
   * @param req
   * @return
   */
  @PostMapping("/create")
  @MerchantUser
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
  R<Page<PosTerminalPriceTemplateResp>> listTerminalPriceTemplate(@RequestBody TerminalPriceTemplateQueryReq req){
    PagingCondition pagingCondition = new PagingCondition();
    pagingCondition.setPageNo(req.getCurrent());
    pagingCondition.setPageSize(req.getSize());
    TerminalPriceTemplateReq posPriceTemplateReq = TerminalPriceTemplateReq.builder()
            .storeCodes(Lists.newArrayList(req.getStoreCode()))
            .paging(pagingCondition)
            .build();
    return success(cbestDmallService.listTerminalPriceTemplate(posPriceTemplateReq));
  }

  /**
   * 修改收银机价格模板
   * @param req
   * @return
   */
  @PostMapping("/terminal/modify")
  @MerchantUser
  R<PosTerminalPriceTemplateResp> modifyTerminalPriceTemplate(@RequestBody TerminalPriceTemplateUpdateReq req) {
    return success(cbestDmallService.modifyTerminalPriceTemplate(req));
  }
}
