package com.welfare.servicemerchant.controller;

import static net.dreamlu.mica.core.result.R.success;

import com.welfare.service.remote.PlatformUserFeignClient;
import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.remote.entity.PlatformUserDataResponse;
import com.welfare.service.remote.entity.PlatformUserResponse;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
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
  private final static Integer size = 1000;
  private final FileUploadService fileUploadService;

  /**
   * 获取商户用户列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ApiOperation("获取商户用户列表")
  PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
      @RequestParam int current,
      @RequestParam int size,
      @RequestParam(required = false) String merchant_code,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) Date start_create_time,
      @RequestParam(required = false) Date end_create_time
  ) {
    return platformUserFeignClient.getPlatformUserList(
        size, current, merchant_code, username, status, start_create_time, end_create_time);
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
  @RequestMapping(value = "/detail", method = RequestMethod.GET)
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


  /**
   * 导出商户用户列表
   */
  @RequestMapping(value = "/export", method = RequestMethod.GET)
  @ApiOperation("导出商户用户列表")
  R<String> export(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String merchant_code,
      @RequestParam(required = false) Date start_create_time,
      @RequestParam(required = false) Date end_create_time
  ) throws IOException {
    List<PlatformUser> platformUserList = new ArrayList<>();

    Integer current = 1;
    while (true) {
      try {
        Thread.sleep(3 * 1000);
        PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> response = platformUserFeignClient
            .getPlatformUserList(
                size, current, merchant_code, username, status, start_create_time, end_create_time);
        if (response == null || !response.getCode().equals(200) || CollectionUtils.isEmpty(
            response.getData().getList())) {
          break;
        }

        current++;
        platformUserList.addAll(response.getData().getList());

      } catch (Exception e) {
        platformUserList.clear();
        log.error("导出商户列表失败", e.getMessage());
        break;

      }
    }

    String path = fileUploadService.uploadExcelFile(
        platformUserList, PlatformUser.class, "商户用户列表");
    return success(fileUploadService.getFileServerUrl(path));

  }
}
