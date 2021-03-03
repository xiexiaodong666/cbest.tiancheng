package com.welfare.servicesettlement.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.EmployeeSettleBuildReq;
import com.welfare.service.dto.EmployeeSettleFinishReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/3 4:31 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/employee")
@Api(tags = "员工授信结算相关")
public class EmployeeSettleController {

  @PostMapping("/build")
  @ApiOperation("按条件生成结算单,并返回结算单号")
  @MerchantUser
  public R<List<String>> buildSettle(@RequestBody EmployeeSettleBuildReq employeeSettleBuildReq){
    return null;
  }

  @PostMapping("/finish")
  @ApiOperation("完成结算")
  @MerchantUser
  public R<Boolean> finishSettle(@RequestBody EmployeeSettleFinishReq employeeSettleFinishReq){
    return null;
  }
}
