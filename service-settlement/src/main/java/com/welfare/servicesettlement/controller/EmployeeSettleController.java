package com.welfare.servicesettlement.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.service.dto.EmployeeSettleBuildReq;
import com.welfare.service.dto.EmployeeSettleFinishReq;
import com.welfare.service.dto.EmployeeSettlePageReq;
import com.welfare.service.dto.EmployeeSettleResp;
import com.welfare.service.dto.MonthSettlePageReq;
import com.welfare.service.dto.MonthSettleResp;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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


  @GetMapping("/page")
  @ApiOperation("员工授信消费查询")
  public R<BasePageVo<EmployeeSettleResp>> pageQuery(EmployeeSettlePageReq employeeSettlePageReq){

    return null;
  }

  @GetMapping("/summary")
  @ApiOperation("员工授信消费查询summary")
  public R<EmployeeSettleSumDTO> summary(EmployeeSettleSumReq employeeSettleSumReq){
    return null;

  }

  @GetMapping("/detail/{id}")
  @ApiOperation("员工授信消费明细列表")
  public R<Page<EmployeeSettleDetailResp>> pageQueryDetail(@PathVariable String id,
                                                           EmployeeSettleDetailPageReq employeeSettleDetailPageReq){
    return null;
  }

  @GetMapping("/detail/{id}/export")
  @ApiOperation("员工授信消费明细导出")
  public Object exportDetail(@PathVariable String id,
                                        EmployeeSettleDetailReq employeeSettleDetailReq,
                                        HttpServletResponse response){
    return null;
  }


  @GetMapping("/bill/page")
  @ApiOperation("分页查询用户结算账单列表")
  public R<BasePageVo<EmployeeSettleBillResp>> pageQueryBill(EmployeeSettlePageReq employeeSettlePageReq){
      return null;
  }

  @GetMapping("bill/{settleId}/detail")
  @ApiOperation("分页查询用户结算账单明细列表")
  public R<Page<EmployeeSettleDetailResp>> pageQueryEmployeeSettleDetail(@PathVariable("settleId")Long settleId, EmployeeSettleDetailReq employeeSettlePageReq){
      return null;
  }

  @GetMapping("bill/{settleId}/detail/export")
  @ApiOperation("员工授信消费账单明细导出")
  public Object exportEmployeeSettleDetail(@PathVariable("settleId")Long settleId, EmployeeSettleDetailReq employeeSettleDetailReq, HttpServletResponse response){
      return null;
  }

  @GetMapping("bill/{settleId}/summary")
  @ApiOperation("员工授信消费账单详情查询summary")
  public R<EmployeeSettleSumDTO> employeeSettleDetailSummery(@PathVariable("settleId")Long settleId,EmployeeSettleSumReq employeeSettleSumReq){
    return null;
  }

}
