package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.MerchantReq;
import com.welfare.servicemerchant.dto.MerchantInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 10:33 上午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/messagePushConfig")
@Api(tags = "离线订单推送消息相关")
public class MessagePushConfigController {

  @GetMapping("/contact/list")
  @ApiOperation("获取配置列表")
  @MerchantUser
  public R<List<MerchantInfo>> list(@ApiParam(name = "手机号") String contact) {
    return null;
  }
}
