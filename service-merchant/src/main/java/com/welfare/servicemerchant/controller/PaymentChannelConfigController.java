package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.PaymentChannelConfigService;
import com.welfare.service.dto.paymentChannel.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:34 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/paymentChannelConfig")
@Api(tags = "支付渠道配置控制器")
public class PaymentChannelConfigController {

  @Autowired
  private PaymentChannelConfigService channelConfigService;

  @PostMapping("/simple/page")
  @ApiOperation("分页查询商户的消费门店数量和支付渠道")
  public R<Page<PayChannelConfigSimpleDTO>> page(@RequestBody PayChannelConfigSimpleReq req) {
    return R.success(channelConfigService.simplePage(req));
  }

  @PostMapping("/list")
  @ApiOperation("查询商户所选消费方式的支付渠道的配置")
  public R<PayChannelConfigDetailDTO> detail(@Validated @RequestBody PayChannelConfigReq req) {
    return R.success(channelConfigService.detail(req));
  }

  @PostMapping("/edit")
  @ApiOperation("编辑商户所选消费方式的支付渠道")
  public R<Boolean> edit(@Validated @RequestBody PayChannelConfigEditReq req) {
    return R.success(channelConfigService.edit(req));
  }
}
