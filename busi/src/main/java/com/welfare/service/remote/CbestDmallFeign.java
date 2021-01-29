package com.welfare.service.remote;

import com.welfare.service.remote.entity.pos.*;
import com.welfare.service.remote.fallback.CbestDmallFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 机具相关远程服务
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:36 上午
 */
@FeignClient(value = "CbestDmall-Interface", url = "${cbest.dmall.url:http://dmall.e-cbest.com}", fallbackFactory = CbestDmallFeignFallback.class)
public interface CbestDmallFeign {

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
}
