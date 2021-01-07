package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.servicemerchant.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * 员工账号存款管理
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  1:55 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account-deposit")
@Api(tags = "员工账号存款管理")
public class AccountDepositController implements IController {

  @GetMapping("/apply/page")
  @ApiOperation("分页账号额度申请列表")
  public R<Page<AccountDepositApplyInfo>> page(@RequestParam @ApiParam("当前页") Integer currentPage,
                                               @RequestParam @ApiParam("单页大小") Integer pageSize,
                                               AccountDepositApplyQuery query){
    return null;
  }

  @GetMapping("apply/detail")
  @ApiOperation("查询账号额度申请详情")
  public R<AccountDepositApplyDetailInfo> detail(@RequestParam @ApiParam(name = "账号额度申请id", required = true) Integer id){
    return null;
  }

  @PostMapping("/apply/update")
  @ApiOperation("修改账号额度申请")
  public R<Long> update(@RequestBody BatchDepositApplyRequest requst){
    return null;
  }

  @PostMapping("/apply/export")
  @ApiOperation("导出账号额度申请(返回文件下载地址)")
  public R<String> export(@RequestBody AccountDepositApplyQuery query){
    return null;
  }


  @PostMapping("/apply/approval")
  @ApiOperation("审批账号额度申请")
  public R<Long> approval(@RequestBody AccountDepositApprovalRequest request){
    return null;
  }

  @PostMapping("/apply/save")
  @ApiOperation("新增额度申请(单个/批量)")
  public R<Long> save(@RequestBody BatchDepositApplyRequest requst){
    return null;
  }
}