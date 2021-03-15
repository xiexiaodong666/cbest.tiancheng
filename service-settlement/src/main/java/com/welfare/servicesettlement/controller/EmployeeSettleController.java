package com.welfare.servicesettlement.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.service.dto.EmployeeSettleBillPageReq;
import com.welfare.service.dto.EmployeeSettleBillResp;
import com.welfare.service.dto.EmployeeSettleBuildReq;
import com.welfare.service.dto.EmployeeSettleConsumePageReq;
import com.welfare.service.dto.EmployeeSettleDetailPageReq;
import com.welfare.service.dto.EmployeeSettleDetailReq;
import com.welfare.service.dto.EmployeeSettleDetailResp;
import com.welfare.service.dto.EmployeeSettleFinishReq;
import com.welfare.service.dto.EmployeeSettleSumReq;
import com.welfare.service.dto.StoreCodeNameDTO;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import com.welfare.service.settlement.EmployeeSettleService;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.welfare.common.exception.ExceptionCode.ILLEGALITY_ARGURMENTS;
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

  @Autowired
  private EmployeeSettleService employeeSettleService;
  private final EmployeeSettleDetailService employeeSettleDetailService;
  private final FileUploadServiceUtil fileUploadService;

  @PostMapping("/build")
  @ApiOperation("按条件生成结算单,并返回结算单号")
  @MerchantUser
  public R<List<String>> buildSettle(@RequestBody EmployeeSettleBuildReq employeeSettleBuildReq){
    return success(employeeSettleService.buildEmployeeSettle(employeeSettleBuildReq));
  }

  @PostMapping("/finish")
  @ApiOperation("完成结算")
  @MerchantUser
  public R<Boolean> finishSettle(@RequestBody EmployeeSettleFinishReq employeeSettleFinishReq){
    return success(employeeSettleService.finishEmployeeSettle(employeeSettleFinishReq));
  }


  @PostMapping("/page")
  @ApiOperation("员工授信消费查询")
  @MerchantUser
  public R<BasePageVo<EmployeeSettleConsumeDTO>> pageQuery(@RequestBody EmployeeSettleConsumePageReq employeeSettleConsumePageReq){
     MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
     if(merchantUser!=null && !StringUtils.isEmpty(merchantUser.getMerchantCode())){
         employeeSettleConsumePageReq.setMerCode(merchantUser.getMerchantCode());
     }else {
         throw new BusiException(ILLEGALITY_ARGURMENTS, "账户门店异常", null);
     }
     return success(employeeSettleDetailService.pageQuery(employeeSettleConsumePageReq));
  }

  @PostMapping("/summary")
  @ApiOperation("员工授信消费查询summary")
  @MerchantUser
  public R<EmployeeSettleSumDTO> summary(@RequestBody EmployeeSettleConsumeQuery employeeSettleConsumeQuery){
     MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
     if(merchantUser!=null && !StringUtils.isEmpty(merchantUser.getMerchantCode())){
         employeeSettleConsumeQuery.setMerCode(merchantUser.getMerchantCode());
     }else {
         throw new BusiException(ILLEGALITY_ARGURMENTS, "账户门店异常", null);
     }
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
  @MerchantUser
  public Object exportDetail(@PathVariable String accountCode, EmployeeSettleDetailReq employeeSettleDetailReq){
    List<EmployeeSettleDetailResp> employeeSettleDetailRespList = new ArrayList<>();
    List<EmployeeSettleDetailResp> employeeSettleDetailRespTemp;
    employeeSettleDetailReq.setMinId(0L);
    do {
      employeeSettleDetailRespTemp = employeeSettleDetailService.detailExport(accountCode, employeeSettleDetailReq);
      if(CollectionUtil.isNotEmpty(employeeSettleDetailRespTemp)){
        employeeSettleDetailRespList.addAll(employeeSettleDetailRespTemp);
        employeeSettleDetailReq.setMinId(employeeSettleDetailRespTemp.get(employeeSettleDetailRespTemp.size()-1).getId() + 1);
      }else {
        break;
      }
    }while (true);

    String path = null;
    try {
      path = fileUploadService.uploadExcelFile(
              employeeSettleDetailRespList, EmployeeSettleDetailResp.class, "未结算账单明细");
    } catch (IOException e) {
      throw new BusiException(null, "文件导出异常", null);
    }
    return success(fileUploadService.getFileServerUrl(path));
  }


  @PostMapping("/bill/page")
  @ApiOperation("分页查询员工授信消费账单列表")
  @MerchantUser
  public R<Page<EmployeeSettleBillResp>> pageQueryBill(@RequestBody EmployeeSettleBillPageReq billPageReq){
    MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
    if(merchantUser!=null && !StringUtils.isEmpty(merchantUser.getMerchantCode())){
      billPageReq.setMerCode(merchantUser.getMerchantCode());
    }else {
      throw new BusiException(ILLEGALITY_ARGURMENTS, "账户门店异常", null);
    }
    Page<EmployeeSettleBillResp> page = employeeSettleService.pageQueryBill(billPageReq);
    return success(page);
  }

  @GetMapping("bill/{settleNo}")
  @ApiOperation("分页查询用户消费账单明细列表")
  public R<Page<EmployeeSettleDetailResp>> pageQueryEmployeeSettleDetail(@PathVariable("settleNo")String settleNo, EmployeeSettleDetailPageReq employeeSettlePageReq){
    Page<EmployeeSettleDetailResp> page= employeeSettleDetailService.pageQueryEmployeeSettleDetail(settleNo, employeeSettlePageReq);
    return success(page);
  }

  @GetMapping("bill/{settleNo}/export")
  @ApiOperation("员工授信消费账单明细导出")
  public Object exportEmployeeSettleDetail(@PathVariable("settleNo")String settleNo, EmployeeSettleDetailReq employeeSettleDetailReq, HttpServletResponse response){
    List<EmployeeSettleDetailResp> employeeSettleDetailRespList = new ArrayList<>();
    List<EmployeeSettleDetailResp> employeeSettleDetailRespTemp;
    employeeSettleDetailReq.setMinId(0L);
    do {
      employeeSettleDetailRespTemp = employeeSettleDetailService.detailExportWithSettleNo(settleNo, employeeSettleDetailReq);
      if(CollectionUtil.isNotEmpty(employeeSettleDetailRespTemp)){
        employeeSettleDetailRespList.addAll(employeeSettleDetailRespTemp);
        employeeSettleDetailReq.setMinId(employeeSettleDetailRespTemp.get(employeeSettleDetailRespTemp.size()-1).getId() + 1);
      }else {
        break;
      }
    }while (true);

    String path = null;
    try {
      path = fileUploadService.uploadExcelFile(
          employeeSettleDetailRespList, EmployeeSettleDetailResp.class, "账单明细");
    } catch (IOException e) {
      throw new BusiException(null, "文件导出异常", null);
    }
    return success(fileUploadService.getFileServerUrl(path));
  }

  @GetMapping("bill/{settleNo}/summary")
  @ApiOperation("员工授信消费账单详情查询summary")
  public R<EmployeeSettleSumDTO> employeeSettleDetailSummery(@PathVariable("settleNo")String settleNo,EmployeeSettleDetailReq employeeSettleDetailReq){
    return success(employeeSettleDetailService.detailSummaryWithSettleNo(settleNo, employeeSettleDetailReq));
  }

  @GetMapping("/pullAccountDetailByDate")
  @ApiOperation("员工授信交易数据手工拉取")
  public R settlementDetailDealTask(String dateStr) {
    Date date = null;
    if (StringUtils.isNoneBlank(dateStr)) {
      try {
        date = DateUtil.str2Date(dateStr, DateUtil.DEFAULT_DATE_FORMAT);
      } catch (Exception e) {
        throw new BusiException("时间转换失败");
      }
    }
    employeeSettleDetailService.pullAccountDetailByDate(date);
    return R.success();
  }

    @GetMapping("/stores")
    @ApiOperation("查询员工授信消费所有门店name和code")
    public R<List<StoreCodeNameDTO>> allStoresInMonthSettle(@RequestParam(value = "settleNo", required = false)String settleNo, @RequestParam(value = "accountCode", required = false)String accountCode){
    if (StringUtils.isBlank(settleNo)&&StringUtils.isBlank(accountCode)){
        throw new BusiException(ILLEGALITY_ARGURMENTS, "缺少参数", null);
    }
    List<StoreCodeNameDTO> supplierStores =  employeeSettleDetailService.allStoresInMonthSettle(settleNo, accountCode);
    return success(supplierStores);
    }
}
