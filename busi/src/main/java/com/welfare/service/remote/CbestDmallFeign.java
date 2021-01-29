package com.welfare.service.remote;

import com.welfare.service.remote.entity.pos.DmallResponse;
import com.welfare.service.remote.entity.pos.PosPriceTemplateReq;
import com.welfare.service.remote.entity.pos.PriceTemplateBrief;
import com.welfare.service.remote.fallback.CbestDmallFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 机具相关远程服务
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:36 上午
 */
@FeignClient(value = "CbestDmall-Interface", url = "${cbest.dmall.url:http://dmall.e-cbest.com}", fallbackFactory = CbestDmallFeignFallback.class)
public interface CbestDmallFeign {

  @PostMapping("/orangeapi/manage/price-template/list")
  DmallResponse<PriceTemplateBrief> priceTemplateList(@RequestBody PosPriceTemplateReq req);
}
