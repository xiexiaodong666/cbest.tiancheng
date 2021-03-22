package com.welfare.servicemerchant.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.MerchantService;
import com.welfare.service.MessagePushConfigContactService;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactAddReq;
import com.welfare.service.dto.messagepushconfig.MessagConfigContactEditReq;
import com.welfare.service.dto.messagepushconfig.MessagPushConfigContactDTO;
import com.welfare.service.dto.messagepushconfig.MessagPushConfigExcelDTO;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

  private final MessagePushConfigContactService configContactService;
  private final FileUploadService fileUploadService;
  private final MerchantService merchantService;

  @GetMapping("/contact/list")
  @ApiOperation("获取配置列表")
  @MerchantUser
  public R<List<MessagPushConfigContactDTO>> contactList(@ApiParam(name = "手机号") String contact) {
    return R.success(configContactService.listByContact(contact));
  }

  @PostMapping("/contact/one")
  @ApiOperation("查询单个配置")
  @MerchantUser
  public R<MessagPushConfigContactDTO> oneContact(String id) {
    BizAssert.notBlank(id, ExceptionCode.ILLEGALITY_ARGURMENTS, "id不能为空");
    return R.success(configContactService.findOneById(id));
  }

  @PostMapping("/contact/export")
  @ApiOperation("导出配置列表(返回文件下载地址)")
  @MerchantUser
  public R<String> exportContact(@ApiParam(name = "手机号") String contact) throws IOException {
    List<MessagPushConfigContactDTO> dtos = configContactService.listByContact(contact);
    Merchant merchant = merchantService.getMerchantByMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    String path = fileUploadService.uploadExcelFile(MessagPushConfigExcelDTO.of(dtos, merchant), MessagPushConfigContactDTO.class, "短信配置");
    return R.success(fileUploadService.getFileServerUrl(path));
  }

  @PostMapping("/contact/del")
  @ApiOperation("删除配置")
  @MerchantUser
  public R<Boolean> contactDel(@ApiParam(required = true) String id) {
    BizAssert.notBlank(id, ExceptionCode.ILLEGALITY_ARGURMENTS, "id不能为空");
    return R.success(configContactService.delete(id));
  }

  @PostMapping("/contact/edit")
  @ApiOperation("编辑配置")
  @MerchantUser
  public R<Boolean> contactEdit(@Validated @RequestBody MessagConfigContactEditReq req) {
    return R.success(configContactService.edit(req));
  }

  @PostMapping("/contact/add")
  @ApiOperation("新增配置")
  @MerchantUser
  public R<Boolean> contactAdd(@Validated @RequestBody MessagConfigContactAddReq req) {
    return R.success(configContactService.add(req));
  }
}
