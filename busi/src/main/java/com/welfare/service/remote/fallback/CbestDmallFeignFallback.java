package com.welfare.service.remote.fallback;

import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.CbestDmallFeign;
import com.welfare.service.remote.entity.pos.*;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:50 上午
 */
@Component
@Slf4j
public class CbestDmallFeignFallback implements FallbackFactory<CbestDmallFeign> {
  @Override
  public CbestDmallFeign create(Throwable throwable) {
    return new CbestDmallFeign() {

      @Override
      public DmallResponse<PagingResult<PriceTemplateBrief>> listPriceTemplate(PosPriceTemplateReq req) {
        log.error("分页查询价格模板失败, 请求:{}", JSON.toJSONString(req), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }

      @Override
      public DmallResponse<PosPriceTemplate> queryPriceTemplate(@NotNull(message = "id不能为空") Map<String, Long> id) {
        log.error("查询价格模板失败, 请求:{}", JSON.toJSONString(id), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }

      @Override
      public DmallResponse<PosPriceTemplate> createPriceTemplate(PosPriceTemplateSaveReq req) {
        log.error("创建价格模板失败, 请求:{}", JSON.toJSONString(req), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }

      @Override
      public DmallResponse<PosPriceTemplate> modifyPriceTemplate(PosPriceTemplate req) {
        log.error("修改价格模板失败, 请求:{}", JSON.toJSONString(req), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }

      @Override
      public DmallResponse<PagingResult<PosTerminalPriceTemplateResp>> listTerminalPriceTemplate(TerminalPriceTemplateReq req) {
        log.error("分页查询收银机价格模板失败, 请求:{}", JSON.toJSONString(req), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }

      @Override
      public DmallResponse<PosTerminalPriceTemplateResp> modifyTerminalPriceTemplate(TerminalPriceTemplateUpdateReq req) {
        log.error("修改收银机价格模板失败, 请求:{}", JSON.toJSONString(req), throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
      }
    };
  }
}
