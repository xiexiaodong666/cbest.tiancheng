package com.welfare.service.remote.fallback;

import com.welfare.service.remote.PlatformUserFeignClient;
import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.remote.entity.PlatformUserDataResponse;
import com.welfare.service.remote.entity.PlatformUserResponse;
import feign.hystrix.FallbackFactory;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:59 AM
 */
@Slf4j
@Component
public class PlatformUserFallback implements FallbackFactory<PlatformUserFeignClient> {

  @Override
  public PlatformUserFeignClient create(Throwable cause) {
    return new PlatformUserFeignClient(){

      @Override
      public PlatformUserResponse<PlatformUserDataResponse<PlatformUser>> getPlatformUserList(
          int pageSize, int page, String merchat_code, Date start_create_time, Date end_create_time) {
        log.error("获取商户用户失败",cause);
        PlatformUserResponse response= new PlatformUserResponse(500,cause.getMessage(),null);
        return response;
      }

      @Override
      public PlatformUserResponse<Boolean> addPlatformUser(PlatformUser platformUser) {
        log.error("新增商户用户失败",cause);
        PlatformUserResponse response= new PlatformUserResponse(500,cause.getMessage(),null);
        return response;
      }

      @Override
      public PlatformUserResponse<Boolean> updatePlatformUser(PlatformUser platformUser) {
        log.error("修改商户用户失败",cause);
        PlatformUserResponse response= new PlatformUserResponse(500,cause.getMessage(),null);
        return response;
      }

      @Override
      public PlatformUserResponse<PlatformUser> getPlatformUserDetail(Long id) {
        log.error("获取商户用户详情失败",cause);
        PlatformUserResponse response= new PlatformUserResponse(500,cause.getMessage(),null);
        return response;
      }

      @Override
      public PlatformUserResponse<Boolean> updatePlatformUserStatus(
          PlatformUser platformUser) {
        log.error("获取商户用户状态失败",cause);
        PlatformUserResponse response= new PlatformUserResponse(500,cause.getMessage(),null);
        return response;
      }
    };
  }
}
