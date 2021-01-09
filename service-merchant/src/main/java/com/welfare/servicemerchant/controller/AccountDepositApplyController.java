package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.service.AccountDepositApplyService;
import com.welfare.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/account-deposit-apply")
@Api(tags = "员工账号存款管理")
public class AccountDepositApplyController implements IController {

  @Autowired
  private AccountDepositApplyService depositApplyService;

  @GetMapping("/page")
  @ApiOperation("分页账号额度申请列表")
  @MerchantUser
  public R<Page<AccountDepositApplyInfo>> page(@RequestParam @ApiParam("当前页（从1开始）") Integer current,
                                               @RequestParam @ApiParam("单页大小") Integer size,
                                               AccountDepositApplyQuery query){
    return null;
  }

  @GetMapping("/detail")
  @ApiOperation("查询账号额度申请详情")
  @MerchantUser
  public R<AccountDepositApplyDetailInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Integer id){
    return null;
  }

  @PostMapping("/update")
  @ApiOperation("修改账号额度申请(单个)")
  @MerchantUser
  public R<Long> update(@RequestBody DepositApplyUpdateRequest requst){
    return null;
  }

  @PostMapping("/batch-update")
  @ApiOperation("修改账号额度申请(批量)")
  @MerchantUser
  public R<Long> batchUpdate(@RequestParam @ApiParam(name = "申请id）",required = true) Long id,
                             @RequestParam @ApiParam("文件id") String fileId,
                             @RequestParam @ApiParam("申请备注") String applyRemark,
                             @RequestParam @ApiParam(name = "福利类型",required = true) String merAccountTypeCode) {
    return null;
  }

  @PostMapping("/export")
  @ApiOperation("导出账号额度申请(返回文件下载地址)")
  @MerchantUser
  public R<String> export(@RequestBody AccountDepositApplyQuery query){
    return null;
  }

  @PostMapping("/approval")
  @ApiOperation("审批账号额度申请")
  @MerchantUser
  public R<Long> approval(@RequestBody AccountDepositApprovalRequest request){
    return null;
  }

  @PostMapping("/save")
  @ApiOperation("新增额度申请(单个)")
  @MerchantUser
  public R<Long> save(@RequestBody DepositApplyRequest request){

    return null;
  }

  @PostMapping("/batch-save")
  @ApiOperation("新增额度申请(批量)")
  @MerchantUser
  public R<Long> batchSave(@RequestParam @ApiParam(name = "请求id（用于幂等处理，UUID即可）",required = true) String requestId,
                           @RequestParam @ApiParam("文件id")String fileId,
                           @RequestParam @ApiParam("申请备注") String applyRemark,
                           @RequestParam @ApiParam(name = "福利类型",required = true) String merAccountTypeCode
                           ){
    return null;
  }

  @PostMapping("/upload")
  @ApiOperation("上传申请excel文件(上传后返回fileId)")
  public R<String> upload(@RequestParam(name = "file")@ApiParam("file")MultipartFile multipartFile) {
    return null;
  }

  @GetMapping("/upload/page")
  @ApiOperation("分页查询上传excel的数据")
  public R<Page<TempAccountDepositApplyDTO>> uploadData(@RequestParam @ApiParam("当前页(从1开始)") Integer current,
                                                        @RequestParam @ApiParam("单页大小") Integer size,
                                                        @RequestParam @ApiParam("文件id")String fileId) {
    return null;
  }
}