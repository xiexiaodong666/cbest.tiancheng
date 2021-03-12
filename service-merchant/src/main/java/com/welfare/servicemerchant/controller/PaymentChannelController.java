package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.PaymentChannelDTO;
import com.welfare.service.dto.PaymentChannelReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/11 5:13 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/paymentChannel")
@Api(tags = "支付渠道配置控制器")
public class PaymentChannelController {

  @PostMapping("/list")
  @ApiOperation("根据商户编码查询支付渠道配列表")
  public R<List<PaymentChannelDTO>> list(@RequestBody PaymentChannelReq req) {
    return R.success(null);
  }

}
