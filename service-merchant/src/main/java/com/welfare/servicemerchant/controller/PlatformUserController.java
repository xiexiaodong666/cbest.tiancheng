package com.welfare.servicemerchant.controller;

import static net.dreamlu.mica.core.result.R.success;

import com.welfare.common.annotation.ApiUser;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.service.remote.PlatformUserFeignClient;
import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.remote.entity.PlatformUserDataResponse;
import com.welfare.service.remote.entity.PlatformUserResponse;
import com.welfare.service.remote.entity.ShoppingPlatformUser;
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
  @ApiUser
  PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
      @RequestParam int current,
      @RequestParam int size,
      @RequestParam(required = false) String merchant_code,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String start_create_time,
      @RequestParam(required = false) String end_create_time
  ) {
    PlatformUserResponse<PlatformUserDataResponse<ShoppingPlatformUser>> response = platformUserFeignClient.getPlatformUserList(
        size, current, merchant_code, username, status, start_create_time, end_create_time);


    PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> responsePlatformUserResponse = new PlatformUserResponse();
    List<ShoppingPlatformUser> shoppingPlatformUsers = response.getData().getList();
    List<PlatformUser> platformUsers = new ArrayList<>(shoppingPlatformUsers.size());
    PlatformUserDataResponse platformUserDataResponse = new PlatformUserDataResponse();
    platformUserDataResponse.setTotal(response.getData().getTotal());
    platformUserDataResponse.setList(platformUsers);
    responsePlatformUserResponse.setCode(response.getCode());
    responsePlatformUserResponse.setMessage(response.getMessage());
    responsePlatformUserResponse.setData(platformUserDataResponse);

    for (ShoppingPlatformUser s:
    shoppingPlatformUsers) {
      platformUsers.add(transferPlatformUser(s));
    }
    return responsePlatformUserResponse;
  }

  /**
   * 新增商户用户
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("新增商户用户")
  @ApiUser
  PlatformUserResponse<Boolean> addPlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.addPlatformUser(transferShoppingPlatformUser(platformUser, 1));
  }

  /**
   * 修改商户用户
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("修改商户用户")
  @ApiUser
  PlatformUserResponse<Boolean> updatePlatformUser(@RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUser(transferShoppingPlatformUser(platformUser,2));
  }


  /**
   * 详情
   */
  @RequestMapping(value = "/detail", method = RequestMethod.GET)
  @ApiOperation("详情")
  @ApiUser
  PlatformUserResponse<PlatformUser> getPlatformUserDetail(
      @RequestParam("id") Long id) {
    PlatformUserResponse<ShoppingPlatformUser> response = platformUserFeignClient
        .getPlatformUserDetail(id);
    PlatformUserResponse<PlatformUser> platformUserPlatformUserResponse = new PlatformUserResponse<>();
    platformUserPlatformUserResponse.setCode(response.getCode());
    platformUserPlatformUserResponse.setMessage(response.getMessage());
    platformUserPlatformUserResponse.setData(transferPlatformUser(response.getData()));

    return platformUserPlatformUserResponse;
  }

  /**
   * 锁定/解锁
   */
  @RequestMapping(value = "/update-status", method = RequestMethod.POST, consumes = "application/json")
  @ApiOperation("锁定/解锁")
  @ApiUser
  PlatformUserResponse<Boolean> updatePlatformUserStatus(
      @RequestBody PlatformUser platformUser) {

    return platformUserFeignClient.updatePlatformUserStatus(
        transferShoppingPlatformUser(platformUser, 2));
  }


  /**
   * 导出商户用户列表
   */
  @RequestMapping(value = "/export", method = RequestMethod.GET)
  @ApiOperation("导出商户用户列表")
  @ApiUser
  R<String> export(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String merchant_code,
      @RequestParam(required = false) String start_create_time,
      @RequestParam(required = false) String end_create_time
  ) throws IOException {
    List<ShoppingPlatformUser> platformUserList = new ArrayList<>();

    Integer current = 1;
    while (true) {
      try {
        Thread.sleep(3 * 1000);
        PlatformUserResponse<PlatformUserDataResponse<ShoppingPlatformUser>> response = platformUserFeignClient
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
        log.error("导出商户用户列表失败", e.getMessage());
        break;

      }
    }

    String path = fileUploadService.uploadExcelFile(
        platformUserList, ShoppingPlatformUser.class, "商户用户列表");
    platformUserList.clear();
    return success(fileUploadService.getFileServerUrl(path));

  }

  private PlatformUser transferPlatformUser(ShoppingPlatformUser shoppingPlatformUser) {
    PlatformUser platformUser = new PlatformUser();

    platformUser.setId(shoppingPlatformUser.getId());
    platformUser.setName(shoppingPlatformUser.getName());
    platformUser.setUsername(shoppingPlatformUser.getUsername());
    platformUser.setInitPassword(shoppingPlatformUser.getInit_password());
    platformUser.setMerchantName(shoppingPlatformUser.getMerchant_name());
    platformUser.setMerchantCode(shoppingPlatformUser.getMerchant_code());
    platformUser.setStatus(shoppingPlatformUser.getStatus());
    platformUser.setRemark(shoppingPlatformUser.getRemark());
    platformUser.setCreatedBy(shoppingPlatformUser.getCreated_by());
    platformUser.setUpdatedBy(shoppingPlatformUser.getUpdated_by());
    platformUser.setCreatedAt(shoppingPlatformUser.getCreated_at());

    return platformUser;
  }

  /**
   *
   * @param platformUser
   * @param action 1 新增 2 修改
   * @return
   */
  private ShoppingPlatformUser transferShoppingPlatformUser(PlatformUser platformUser, Integer action) {
    ShoppingPlatformUser shoppingPlatformUser = new ShoppingPlatformUser();

    shoppingPlatformUser.setId(platformUser.getId());
    shoppingPlatformUser.setName(platformUser.getName());
    shoppingPlatformUser.setUsername(platformUser.getUsername());
    shoppingPlatformUser.setInit_password(platformUser.getInitPassword());
    shoppingPlatformUser.setMerchant_name(platformUser.getMerchantName());
    shoppingPlatformUser.setMerchant_code(platformUser.getMerchantCode());
    shoppingPlatformUser.setStatus(platformUser.getStatus());
    shoppingPlatformUser.setRemark(platformUser.getRemark());
    if(action == 1) {
      shoppingPlatformUser.setCreated_by(UserInfoHolder.getUserInfo().getUserName());
    }
    if(action == 2) {
      shoppingPlatformUser.setUpdated_by(UserInfoHolder.getUserInfo().getUserName());
    }

    return shoppingPlatformUser;
  }
}
