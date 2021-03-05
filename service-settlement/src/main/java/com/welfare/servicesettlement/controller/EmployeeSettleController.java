package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.persist.dto.query.EmployeeSettleDetailQuery;
import com.welfare.service.dto.*;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import com.welfare.service.settlement.EmployeeSettleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static net.dreamlu.mica.core.result.R.success;

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

  private final EmployeeSettleService employeeSettleService;
  private final EmployeeSettleDetailService employeeSettleDetailService;

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
  public R<BasePageVo<EmployeeSettleConsumeDTO>> pageQuery(EmployeeSettleConsumePageReq employeeSettleConsumePageReq){
    return success(employeeSettleDetailService.pageQuery(employeeSettleConsumePageReq));
  }

  @GetMapping("/summary")
  @ApiOperation("员工授信消费查询summary")
  public R<EmployeeSettleSumDTO> summary(EmployeeSettleConsumeQuery employeeSettleConsumeQuery){
    return success(employeeSettleDetailService.summary(employeeSettleConsumeQuery));

  }

  @GetMapping("/detail/{accountCode}")
  @ApiOperation("员工授信消费明细列表")
  public R<BasePageVo<EmployeeSettleDetailResp>> pageQueryDetail(@PathVariable String accountCode,
                                                           EmployeeSettleDetailPageReq employeeSettleDetailPageReq){
    return success(employeeSettleDetailService.pageQueryDetail(accountCode, employeeSettleDetailPageReq));
  }

  @GetMapping("/detail/{accountCode}/summary")
  @ApiOperation("员工授信消费明细列表summary")
  public R<EmployeeSettleSumDTO> detailSummary(@PathVariable String accountCode,
                                               EmployeeSettleDetailReq employeeSettleDetailReq){
    return success(employeeSettleDetailService.detailSummary(accountCode, employeeSettleDetailReq));
  }

  @GetMapping("/detail/{accountCode}/export")
  @ApiOperation("员工授信消费明细导出")
  public Object exportDetail(@PathVariable String accountCode,
                                        EmployeeSettleDetailReq employeeSettleDetailReq,
                                        HttpServletResponse response){
    return null;
  }


  @GetMapping("/bill/page")
  @ApiOperation("分页查询员工授信消费账单列表")
  @MerchantUser
  public R<Page<EmployeeSettleBillResp>> pageQueryBill(EmployeeSettleBillPageReq billPageReq){
    MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
    if(merchantUser!=null && !StringUtils.isEmpty(merchantUser.getMerchantCode())){
      billPageReq.setMerCode(merchantUser.getMerchantCode());
    }else {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "账户门店异常", null);
    }
    Page<EmployeeSettleBillResp> page = employeeSettleService.pageQueryBill(billPageReq);
    return R.success(page);
  }

  @GetMapping("bill/{settleId}/detail")
  @ApiOperation("分页查询用户结算账单明细列表")
  public R<Page<EmployeeSettleDetailResp>> pageQueryEmployeeSettleDetail(@PathVariable("settleId")Long settleId, EmployeeSettleDetailPageReq employeeSettlePageReq){
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
