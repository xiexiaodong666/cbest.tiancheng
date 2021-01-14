package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.service.AccountDepositApplyDetailService;
import com.welfare.service.AccountDepositApplyService;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.accountapply.*;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

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
  private AccountDepositApplyDetailService detailService;

  @Autowired
  private FileUploadService fileUploadService;

  @Autowired
  private TempAccountDepositApplyService tempAccountDepositApplyService;

  @Autowired
  @Qualifier("accountApplySaveExecutor")
  private ThreadPoolExecutor executor;

  @PostMapping("/page")
  @ApiOperation("分页账号额度申请列表")
  @MerchantUser
  public R<Page<AccountDepositApplyInfo>> page(@RequestBody AccountDepositApplyQuery query){
    return success(depositApplyService.page(query.getCurrent(), query.getSize() == 0 ? 10 : query.getSize() , query));
  }

  @GetMapping("/detail")
  @ApiOperation("查询账号额度申请详情")
  @MerchantUser
  public R<AccountDepositApplyDetailInfo> detail(@RequestParam @ApiParam(name = "申请id", required = true) Long id){
    return success(depositApplyService.detail(id));
  }

  @GetMapping("/batch-item")
  @ApiOperation("分页查询批量额度申请明细")
  @MerchantUser
  public R<Page<TempAccountDepositApplyDTO>> batchtem(@RequestParam @ApiParam("当前页（从1开始）") Integer current,
                                                      @RequestParam @ApiParam("单页大小") Integer size,
                                                      @RequestParam @ApiParam(name = "申请id", required = true) Long id){
    return success(detailService.pageByApplyCode(id, current, size));
  }

  @GetMapping("/batch-total")
  @ApiOperation("查询批量额度申请的总人数和总金额")
  @MerchantUser
  public R<AccountApplyTotalDTO> batchTotal(@RequestParam @ApiParam(name = "申请id", required = true) Long id){
    return success(detailService.getUserCountAndTotalmount(id));
  }

  @PostMapping("/update")
  @ApiOperation("修改账号额度申请(单个)")
  @MerchantUser
  public R<Long> update(@Validated @RequestBody DepositApplyUpdateRequest requst){
    return success(depositApplyService.updateOne(requst, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/batch-update")
  @ApiOperation("修改账号额度申请(批量)")
  @MerchantUser
  public R<Long> batchUpdate(@RequestParam @ApiParam(name = "申请id）",required = true) Long id,
                             @RequestParam @ApiParam(name = "文件id",required = true) String fileId,
                             @RequestParam(required = false) @ApiParam("申请备注") String applyRemark,
                             @RequestParam(required = false) @ApiParam(name = "福利类型",required = true) String merAccountTypeCode,
                             @RequestParam(required = false) @ApiParam(name = "福利类型名称",required = true) String merAccountTypeName) {
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
  public R<String> export(@Validated@RequestBody AccountDepositApplyQuery query) throws IOException {
    List<AccountDepositApplyExcelInfo> list = depositApplyService.list(query);
    String path = fileUploadService.uploadExcelFile(list, AccountDepositApplyExcelInfo.class, "员工额度申请");
    return success(fileUploadService.getFileServerUrl(path));
  }

  @PostMapping("/approval")
  @ApiOperation("审批账号额度申请")
  @MerchantUser
  public R<Long> approval(@Validated@RequestBody AccountDepositApprovalRequest request){
    return success(depositApplyService.approval(request));
  }

  @PostMapping("/save")
  @ApiOperation("新增额度申请(单个)")
  @MerchantUser
  public R<Long> save(@Validated@RequestBody DepositApplyRequest request){
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
    request.setMerAccountTypeName(merAccountTypeName);
    return success(depositApplyService.saveBatch(request, fileId, MerchantUserHolder.getDeptIds()));
  }

  @PostMapping("/upload")
  @ApiOperation("上传申请excel文件(上传后返回fileId)")
  public R<String> upload(@RequestPart(name = "file")@ApiParam(name = "file",required = true)MultipartFile multipartFile,
                          @RequestParam @ApiParam(name = "请求id（用于幂等处理，UUID即可）",required = true) String requestId) throws IOException {
    return success(tempAccountDepositApplyService.upload(multipartFile, requestId, executor));
  }

  @GetMapping("/upload/page")
  @ApiOperation("分页查询上传excel的数据")
  public R<Page<TempAccountDepositApplyDTO>> uploadData(@RequestParam @ApiParam("当前页(从1开始)") Integer current,
                                                        @RequestParam @ApiParam("单页大小") Integer size,
                                                        @RequestParam @ApiParam("文件id")String fileId) {
    return success(tempAccountDepositApplyService.pageByFileIdByExistAccount(current, size, fileId));
  }

  @GetMapping("/upload/total")
  @ApiOperation("查询上传excel的总人数和总金额")
  public R<AccountApplyTotalDTO> uploadTotalData(@RequestParam @ApiParam("文件id")String fileId) {
    return success(tempAccountDepositApplyService.getUserCountAndTotalmount(fileId));
  }
}