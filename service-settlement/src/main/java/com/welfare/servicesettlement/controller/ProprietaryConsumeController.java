package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.ProprietaryConsumeResp;
import com.welfare.service.dto.WelfareTypeTotalAmountResp;
import com.welfare.service.dto.proprietary.ProprietaryConsumePageReq;
import com.welfare.service.utils.PageReq;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 3:30 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/proprietary/consume")
@Api(tags = "自营消费")
public class ProprietaryConsumeController implements IController {

  @Autowired
  private SettleDetailService settleDetailService;

  @Autowired
  private FileUploadServiceUtil fileUploadServiceUtil;

  @PostMapping("/page")
  @ApiOperation("分页查询自营消费列表")
  @MerchantUser
  public R<Page<ProprietaryConsumeResp>> pageQueryMonthSettleDetail(@RequestBody ProprietaryConsumePageReq req, PageReq pageReq){
    req.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    return success(settleDetailService.queryProprietaryConsumePage(req, pageReq));
  }

  @PostMapping("/totalAmount")
  @ApiOperation("查询所有员工消费支出类MerCooperationModeEnum型的总金额")
  @MerchantUser
  public R<List<WelfareTypeTotalAmountResp>> total(@RequestBody ProprietaryConsumePageReq req){
    req.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    return success(settleDetailService.statisticalAmountGroupByWelfareTypeCode(req));
  }

  @PostMapping("/export")
  @ApiOperation("导出查询自营消费列表(返回文件下载地址)")
  @MerchantUser
  public R<String> export(@RequestBody ProprietaryConsumePageReq query) throws IOException {
    query.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
    List<ProprietaryConsumeResp> list = settleDetailService.queryProprietaryConsume(query);
    list = list == null ? new ArrayList<>() : list;
    String path = fileUploadServiceUtil.uploadExcelFile(list, ProprietaryConsumeResp.class, "自营消费");
    return success(fileUploadServiceUtil.getFileServerUrl(path));
  }
}
