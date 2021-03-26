package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.PaymentChannelService;
import com.welfare.service.dto.PaymentChannelReq;
import com.welfare.service.dto.paymentChannel.PaymentChannelMerchantDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelSortReq;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
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
@Api(tags = "支付渠道排序控制器")
public class PaymentChannelController {

  @Autowired
  private PaymentChannelService paymentChannelService;

  /**
   * 下拉框
   * @param req
   * @return
   */
  @PostMapping("/list")
  @ApiOperation("根据商户编码查询支付渠道配列表")
  public R<List<com.welfare.service.dto.PaymentChannelDTO>> list(@RequestBody PaymentChannelReq req) {
    return R.success(paymentChannelService.list(req));
  }

  @GetMapping("/default")
  @ApiOperation("查询默认的支付渠道")
  public R<List<PaymentChannelDTO>> defaultList() {
    return R.success(paymentChannelService.defaultList());
  }

  @PostMapping("/merchant/page")
  @ApiOperation("查询已配置独立支付渠道或已排序的商户")
  public R<Page<PaymentChannelMerchantDTO>> merchantSpecialList(@RequestBody PageReq req) {
    return R.success(paymentChannelService.merchantPayChannelList(req));
  }

  @GetMapping("/merchant/no-config/one")
  @ApiOperation("根据商户编码查询还未配置的支付渠道")
  public R<List<PaymentChannelDTO>> defaultNoConfigListByMerCode(@ApiParam(name = "merCode",required = true)
                                                                     @RequestParam("merCode") String merCode) {
    return R.success(paymentChannelService.defaultListByMerCode(merCode));
  }

  @GetMapping("/merchant/one")
  @ApiOperation("根据商户编码查询支付渠道配列表")
  public R<List<PaymentChannelDTO>> merchantSpecialOne(@ApiParam(name = "merCode",required = true)
                                                       @RequestParam("merCode") String merCode) {
    return R.success(paymentChannelService.specialListByMerCode(merCode));
  }

  @DeleteMapping("/merchant/del/{merchantCode}")
  @ApiOperation("删除某个商户的排序配置")
  public R<Boolean> del(@PathVariable("merchantCode") String merchantCode) {
    if (StringUtils.isEmpty(merchantCode)) {
      R.fail("商户编码不能为空");
    }
    return R.success(paymentChannelService.delByMerCode(merchantCode));
  }

  @PostMapping("/sort")
  @ApiOperation("排序")
  public R<Boolean> sort(@Validated @RequestBody @NotEmpty(message = "参数不能为空")
                                              List<PaymentChannelSortReq> req) {
    return R.success(paymentChannelService.sort(req));
  }
}
