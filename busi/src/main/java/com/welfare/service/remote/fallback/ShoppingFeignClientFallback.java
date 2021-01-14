package com.welfare.service.remote.fallback;

import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.EmployerReqDTO;
import com.welfare.service.remote.entity.MerchantShoppingReq;
import com.welfare.service.remote.entity.RoleConsumptionReq;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreShoppingReq;
import com.welfare.service.remote.entity.UserRoleBindingReqDTO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:15 PM
 */
@Slf4j
@Component
public class ShoppingFeignClientFallback implements FallbackFactory<ShoppingFeignClient> {

  @Override
  public ShoppingFeignClient create(Throwable cause) {
    return new ShoppingFeignClient() {

      @Override
      public RoleConsumptionResp addOrUpdateRoleConsumption(RoleConsumptionReq roleConsumptionReq) {
        log.error("批量添加、修改消费门店配置失败", cause);
        RoleConsumptionResp response = new RoleConsumptionResp("500", cause.getMessage(), null);
        return response;
      }

      @Override
      public RoleConsumptionResp addOrUpdateEmployer(EmployerReqDTO employerReqDTO) {
        log.error("批量添加、修改员工账号", cause);
        RoleConsumptionResp response = new RoleConsumptionResp("500", cause.getMessage(), null);
        return response;
      }

      @Override
      public RoleConsumptionResp addOrUpdateUserRoleBinding(
          UserRoleBindingReqDTO userRoleBindingReqDTO) {
        log.error("批量添加、修改员工绑定", cause);
        RoleConsumptionResp response = new RoleConsumptionResp("500", cause.getMessage(), null);
        return response;
      }


      @Override
      public RoleConsumptionResp addOrUpdateMerchant(MerchantShoppingReq merchantShoppingReq) {
        log.error("批量添加、修改商户失败", cause);
        RoleConsumptionResp response = new RoleConsumptionResp("500", cause.getMessage(), null);
        return response;      }

      @Override
      public RoleConsumptionResp addOrUpdateStore(StoreShoppingReq storeShoppingReq) {
        log.error("批量添加、修改门店失败", cause);
        RoleConsumptionResp response = new RoleConsumptionResp("500", cause.getMessage(), null);
        return response;      }
    };

  }
}
