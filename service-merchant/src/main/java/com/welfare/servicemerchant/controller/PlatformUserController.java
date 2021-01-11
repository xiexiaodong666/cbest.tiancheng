package com.welfare.servicemerchant.controller;

import com.welfare.service.remote.PlatformUserFeignClient;
import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.remote.entity.PlatformUserDataResponse;
import com.welfare.service.remote.entity.PlatformUserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 10:46 AM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/platformUser")
@Api(tags = "商户用户接口接口")
public class PlatformUserController {

  private final PlatformUserFeignClient platformUserFeignClient;

  /**
   * 获取商户用户列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ApiOperation("获取商户用户列表")
  PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
      @RequestParam int pageSize,
      @RequestParam int page,
      @RequestParam String merchat_code,
      @RequestParam(required = false) Date start_create_time,
      @RequestParam(required = false) Date end_create_time
  ) {
    return platformUserFeignClient.getPlatformUserList(
        pageSize, page, merchat_code, start_create_time, end_create_time);
  }

  /**
   * 新增商户用户
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("新增商户用户")
  PlatformUserResponse<Boolean> addPlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.addPlatformUser(platformUser);
  }

  /**
   * 修改商户用户
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("修改商户用户")
  PlatformUserResponse<Boolean> updatePlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUser(platformUser);
  }


  /**
   * 详情
   */
  @RequestMapping(value = "/detail", method = RequestMethod.GET, consumes = "application/json")
  @ApiOperation("详情")
  PlatformUserResponse<PlatformUser> getPlatformUserDetail(
      @RequestParam("id") Long id) {

    return platformUserFeignClient.getPlatformUserDetail(id);
  }

  /**
   * 锁定/解锁
   */
  @RequestMapping(value = "/update-status", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("锁定/解锁")
  PlatformUserResponse<Boolean> updatePlatformUserStatus(
      @RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUserStatus(platformUser);
  }
}
