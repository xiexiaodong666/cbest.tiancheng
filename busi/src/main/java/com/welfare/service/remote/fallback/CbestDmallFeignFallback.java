package com.welfare.service.remote.fallback;

import com.welfare.service.remote.CbestDmallFeign;
import com.welfare.service.remote.entity.pos.*;
import feign.hystrix.FallbackFactory;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:50 上午
 */
public class CbestDmallFeignFallback implements FallbackFactory<CbestDmallFeign> {
  @Override
  public CbestDmallFeign create(Throwable throwable) {
    return new CbestDmallFeign() {
      @Override
      public DmallResponse<PagingResult<PriceTemplateBrief>> listPriceTemplate(PosPriceTemplateReq req) {
        return null;
      }

      @Override
      public DmallResponse<PosPriceTemplate> queryPriceTemplate(@NotNull(message = "id不能为空") Map<String, Long> id) {
        return null;
      }

      @Override
      public DmallResponse<PosPriceTemplate> createPriceTemplate(PosPriceTemplateSaveReq req) {
        return null;
      }

      @Override
      public DmallResponse<PosPriceTemplate> modifyPriceTemplate(PosPriceTemplate req) {
        return null;
      }

      @Override
      public DmallResponse<PagingResult<PosTerminalPriceTemplateResp>> listTerminalPriceTemplate(TerminalPriceTemplateReq req) {
        return null;
      }

      @Override
      public DmallResponse<PosTerminalPriceTemplateResp> modifyTerminalPriceTemplate(TerminalPriceTemplateUpdateReq req) {
        return null;
      }
    };
  }
}
