package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactAddReq;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactEditReq;
import com.welfare.service.dto.messagepushconfig.MessagPushConfigContactDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
  public R<List<MessagPushConfigContactDTO>> contactList(@ApiParam(name = "手机号") String contact) {
    return null;
  }

  @PostMapping("/contact/one")
  @ApiOperation("查询单个配置")
  @MerchantUser
  public R<MessagPushConfigContactDTO> oneContact(String id) {
    BizAssert.notBlank(id, ExceptionCode.ILLEGALITY_ARGURMENTS, "id不能为空");
    return null;
  }

  @PostMapping("/contact/export")
  @ApiOperation("导出配置列表(返回文件下载地址)")
  @MerchantUser
  public R<String> exportContact(@ApiParam(name = "手机号") String contact) {
    return null;
  }

  @PostMapping("/contact/del")
  @ApiOperation("删除配置")
  @MerchantUser
  public R<Boolean> contactDel(@ApiParam(required = true) String id) {
    BizAssert.notBlank(id, ExceptionCode.ILLEGALITY_ARGURMENTS, "id不能为空");
    return null;
  }

  @PostMapping("/contact/edit")
  @ApiOperation("编辑配置")
  @MerchantUser
  public R<Boolean> contactEdit(@Validated @RequestBody MessagConfigContactEditReq req) {
    return null;
  }

  @PostMapping("/contact/add")
  @ApiOperation("新增配置")
  @MerchantUser
  public R<Boolean> contactAdd(@Validated @RequestBody MessagConfigContactAddReq req) {
    return null;
  }
}
