package com.welfare.service.remote;

import com.welfare.service.remote.entity.*;
import com.welfare.service.remote.fallback.ShoppingFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "shopping", url = "${shopping.url:http://test-welfare-internal.kube.cbestcd.com/backend-api/rpc-base}", fallbackFactory = ShoppingFeignClientFallback.class)
public interface ShoppingFeignClient {


  /**
   * 批量添加、修改消费门店配置
   */
  @RequestMapping(value = "/inward/tc/addOrUpdateRoleConsumption", method = RequestMethod.POST, consumes = "application/json")
  RoleConsumptionResp addOrUpdateRoleConsumption(
      @RequestBody RoleConsumptionReq roleConsumptionReq);

  /**
   * 批量添加、修改员工账号
   * @param employerReqDTO
   * @return
   */
  @RequestMapping(value = "/inward/tc/addOrUpdateEmployer", method = RequestMethod.POST, consumes = "application/json")
  RoleConsumptionResp addOrUpdateEmployer(
      @RequestBody EmployerReqDTO employerReqDTO);


  /**
   * 批量添加、修改员工账号
   * @param userRoleBindingReqDTO
   * @return
   */
  @RequestMapping(value = "/inward/tc/addOrUpdateUserRoleBinding", method = RequestMethod.POST, consumes = "application/json")
  RoleConsumptionResp addOrUpdateUserRoleBinding(
      @RequestBody UserRoleBindingReqDTO userRoleBindingReqDTO);


  /**
   * 批量添加、修改商户
   */
  @RequestMapping(value = "/inward/tc/addOrUpdateMerchant", method = RequestMethod.POST, consumes = "application/json")
  RoleConsumptionResp addOrUpdateMerchant(
          @RequestBody MerchantShoppingReq merchantShoppingReq);

  /**
   * 批量添加、修改门店
   */
  @RequestMapping(value = "/inward/tc/addOrUpdateStore", method = RequestMethod.POST, consumes = "application/json")
  RoleConsumptionResp addOrUpdateStore(
          @RequestBody StoreShoppingReq storeShoppingReq);

}
