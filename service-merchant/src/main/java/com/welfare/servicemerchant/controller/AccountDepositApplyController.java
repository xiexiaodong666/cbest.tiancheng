package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.service.AccountDepositApplyService;
import com.welfare.service.dto.*;
import com.welfare.servicemerchant.service.FileUploadService;
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

import java.awt.*;
import java.io.IOException;
import java.util.List;

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

  @Autowired
  private FileUploadService fileUploadService;

  @GetMapping("/page")
  @ApiOperation("分页账号额度申请列表")
  @MerchantUser
  public R<Page<AccountDepositApplyInfo>> page(@RequestParam @ApiParam("当前页（从1开始）") Integer current,
                                               @RequestParam @ApiParam("单页大小") Integer size,
                                               AccountDepositApplyQuery query){
    return success(depositApplyService.page(current, size, query));
  }

  @GetMapping("/detail")
  @ApiOperation("查询账号额度申请详情")
  @MerchantUser
  public R<AccountDepositApplyDetailInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Integer id){
    return success(depositApplyService.detail(id));
  }

  @PostMapping("/update")
  @ApiOperation("修改账号额度申请(单个)")
  @MerchantUser
  public R<Long> update(@RequestBody DepositApplyUpdateRequest requst){
    return success(depositApplyService.updateOne(requst, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/batch-update")
  @ApiOperation("修改账号额度申请(批量)")
  @MerchantUser
  public R<Long> batchUpdate(@RequestParam @ApiParam(name = "申请id）",required = true) Long id,
                             @RequestParam @ApiParam(name = "文件id",required = true) String fileId,
                             @RequestParam @ApiParam("申请备注") String applyRemark,
                             @RequestParam @ApiParam(name = "福利类型",required = true) String merAccountTypeCode,
                             @RequestParam @ApiParam(name = "福利类型",required = true) String merAccountTypeName) {
    DepositApplyUpdateRequest requst = new DepositApplyUpdateRequest();
    requst.setId(id);
    requst.setApplyRemark(applyRemark);
    requst.setMerAccountTypeCode(merAccountTypeCode);
    requst.setMerAccountTypeName(merAccountTypeName);
    return success(depositApplyService.updateBatch(requst, fileId, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/export")
  @ApiOperation("导出账号额度申请(返回文件下载地址)")
  @MerchantUser
  public R<String> export(@RequestBody AccountDepositApplyQuery query) throws IOException {
    List<AccountDepositApplyInfo> list = depositApplyService.list(query);
    return success(fileUploadService.uploadExcelFile(list, AccountDepositApplyInfo.class, "员工额度申请"));
  }

  @PostMapping("/approval")
  @ApiOperation("审批账号额度申请")
  @MerchantUser
  public R<Long> approval(@RequestBody AccountDepositApprovalRequest request){
    return success(depositApplyService.approval(request));
  }

  @PostMapping("/save")
  @ApiOperation("新增额度申请(单个)")
  @MerchantUser
  public R<Long> save(@RequestBody DepositApplyRequest request){
    return success(depositApplyService.saveOne(request, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/batch-save")
  @ApiOperation("新增额度申请(批量)")
  @MerchantUser
  public R<Long> batchSave(@RequestParam @ApiParam(name = "请求id（用于幂等处理，UUID即可）",required = true) String requestId,
                           @RequestParam @ApiParam(name = "文件id",required = true)String fileId,
                           @RequestParam @ApiParam("申请备注") String applyRemark,
                           @RequestParam @ApiParam(name = "福利类型",required = true) String merAccountTypeCode,
                           @RequestParam @ApiParam(name = "福利类型名称",required = true) String merAccountTypeName
                           ){
    DepositApplyRequest request = new DepositApplyRequest();
    request.setRequestId(requestId);
    request.setApplyRemark(applyRemark);
    request.setMerAccountTypeCode(merAccountTypeCode);
    request.setMerAccountTypeCode(merAccountTypeName);
    return success(depositApplyService.saveBatch(request, fileId, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/upload")
  @ApiOperation("上传申请excel文件(上传后返回fileId)")
  public R<String> upload(@RequestPart(name = "file")@ApiParam(name = "file",required = true)MultipartFile multipartFile,
                          @RequestParam @ApiParam(name = "请求id（用于幂等处理，UUID即可）",required = true) String requestId) {
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