package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
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

}
