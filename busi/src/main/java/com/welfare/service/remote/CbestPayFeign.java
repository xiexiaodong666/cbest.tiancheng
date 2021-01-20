package com.welfare.service.remote;

import com.welfare.service.remote.entity.CbestPayBaseReq;
import com.welfare.service.remote.entity.CbestPayBaseResp;
import com.welfare.service.remote.fallback.CbestPayFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "enjoyCard-interface", url = "${cbest.pay.url}", fallbackFactory = CbestPayFallback.class)
public interface CbestPayFeign {

    @PostMapping
    CbestPayBaseResp exec(@RequestBody CbestPayBaseReq req);

}
