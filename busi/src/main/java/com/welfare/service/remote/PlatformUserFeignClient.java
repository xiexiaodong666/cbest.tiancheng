package com.welfare.service.remote;

import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.remote.entity.PlatformUserDataResponse;
import com.welfare.service.remote.entity.PlatformUserResponse;
import com.welfare.service.remote.fallback.PlatformUserFallback;
import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "platformUser", url = "${platformUser.url}", fallbackFactory = PlatformUserFallback.class)
public interface PlatformUserFeignClient {

  /**
   * 获取商户用户列表
   */
  @RequestMapping(value = "/api/platform/user/list", method = RequestMethod.GET)
  PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
      @RequestParam("pageSize") int pageSize,
      @RequestParam("page") int page,
      @RequestParam("merchant_code") String merchant_code,
      @RequestParam("start_create_time") Date start_create_time,
      @RequestParam("end_create_time") Date end_create_time
  );

  /**
   * 新增商户用户
   */
  @RequestMapping(value = "/api/platform/user/create", method = RequestMethod.POST, consumes = "application/json")
  PlatformUserResponse<Boolean> addPlatformUser(@RequestBody PlatformUser platformUser);

  /**
   * 修改商户用户
   */
  @RequestMapping(value = "/api/platform/user/update", method = RequestMethod.POST, consumes = "application/json")
  PlatformUserResponse<Boolean> updatePlatformUser(@RequestBody PlatformUser platformUser);


  /**
   * 详情
   */
  @RequestMapping(value = "/api/platform/user/detail", method = RequestMethod.GET, consumes = "application/json")
  PlatformUserResponse<PlatformUser> getPlatformUserDetail(
      @RequestParam("id") Long id);

  /**
   * 锁定/解锁
   */
  @RequestMapping(value = "/api/platform/user/update-status", method = RequestMethod.POST, consumes = "application/json")
  PlatformUserResponse<Boolean> updatePlatformUserStatus(@RequestBody PlatformUser platformUser);



}
