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
@RequestMapping("/merchantStoreRelation")
@Api(tags = "商户用户接口接口")
public class PlatformUserController {

  private final PlatformUserFeignClient platformUserFeignClient;

  /**
   * 获取商户用户列表
   */
  @RequestMapping(value = "/api/platform/user/list", method = RequestMethod.GET)
  @ApiOperation("获取商户用户列表")
  PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
      @RequestParam("pageSize") int pageSize,
      @RequestParam("page") int page,
      @RequestParam("merchat_id") Long merchat_id,
      @RequestParam("start_create_time") Date start_create_time,
      @RequestParam("end_create_time") Date end_create_time
  ) {
    return platformUserFeignClient.getPlatformUserList(
        pageSize, page, merchat_id, start_create_time, end_create_time);
  }

  /**
   * 新增商户用户
   */
  @RequestMapping(value = "/api/platform/user/create", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("新增商户用户")
  PlatformUserResponse<PlatformUser> addPlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.addPlatformUser(platformUser);
  }

  /**
   * 修改商户用户
   */
  @RequestMapping(value = "/api/platform/user/update", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("修改商户用户")
  PlatformUserResponse<PlatformUser> updatePlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUser(platformUser);
  }


  /**
   * 详情
   */
  @RequestMapping(value = "/api/platform/user/detail", method = RequestMethod.GET, consumes = "application/json")
  @ApiOperation("详情")
  PlatformUserResponse<PlatformUser> getPlatformUserDetail(
      @RequestParam("id") Long id) {

    return platformUserFeignClient.getPlatformUserDetail(id);
  }

  /**
   * 锁定/解锁
   */
  @RequestMapping(value = "/api/platform/user/update-status", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("锁定/解锁")
  PlatformUserResponse<PlatformUser> updatePlatformUserStatus(
      @RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUserStatus(platformUser);
  }
}
