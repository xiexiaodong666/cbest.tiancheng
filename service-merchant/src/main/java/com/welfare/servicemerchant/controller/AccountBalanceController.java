package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.servicemerchant.dto.AccountInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工账号余额管理
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  1:55 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account-balance")
@Api(tags = "员工账号余额管理")
public class AccountBalanceController implements IController {

  @GetMapping("/apply/page")
  @ApiOperation("分页账号额度申请列表")
  public R<Page<AccountInfo>> page(@RequestParam @ApiParam("当前页") Integer currentPage,
                                   @RequestParam @ApiParam("单页大小") Integer pageSize,
                                   @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                   @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }


  @GetMapping("apply/detail")
  @ApiOperation("查询账号额度申请详情")
  public R<Page<AccountInfo>> detail(@RequestParam @ApiParam("当前页") Integer currentPage,
                                     @RequestParam @ApiParam("单页大小") Integer pageSize,
                                     @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                     @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }

  @GetMapping("/apply/update")
  @ApiOperation("修改账号额度申请")
  public R<Page<AccountInfo>> update(@RequestParam @ApiParam("当前页") Integer currentPage,
                                     @RequestParam @ApiParam("单页大小") Integer pageSize,
                                     @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                     @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }

  @GetMapping("/apply/export")
  @ApiOperation("导出账号额度申请")
  public R<Page<AccountInfo>> export(@RequestParam @ApiParam("当前页") Integer currentPage,
                                     @RequestParam @ApiParam("单页大小") Integer pageSize,
                                     @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                     @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }


  @GetMapping("/apply/approval")
  @ApiOperation("审批账号额度申请")
  public R<Page<AccountInfo>> approval(@RequestParam @ApiParam("当前页") Integer currentPage,
                                     @RequestParam @ApiParam("单页大小") Integer pageSize,
                                     @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                     @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }

  @GetMapping("/apply/save")
  @ApiOperation("新增额度申请")
  public R<Page<AccountInfo>> save(@RequestParam @ApiParam("当前页") Integer currentPage,
                                       @RequestParam @ApiParam("单页大小") Integer pageSize,
                                       @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                       @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }

  @GetMapping("/apply/batch-save")
  @ApiOperation("批量新增额度申请")
  public R<Page<AccountInfo>> batchSave(@RequestParam @ApiParam("当前页") Integer currentPage,
                                       @RequestParam @ApiParam("单页大小") Integer pageSize,
                                       @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                       @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }
}