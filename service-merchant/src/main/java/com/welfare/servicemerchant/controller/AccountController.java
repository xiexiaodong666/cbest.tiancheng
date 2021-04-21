package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.annotation.RepeatRequestVerification;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.service.AccountService;
import com.welfare.service.dto.*;
import com.welfare.service.remote.entity.EmployerReqDTO;
import com.welfare.servicemerchant.dto.UpdateStatusReq;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @date 2021/1/6  5:16 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Api(tags = "员工账号管理")
public class AccountController implements IController {

  private final AccountService accountService;
  private final FileUploadService fileUploadService;


  @PostMapping("/upload/batch/img")
  @ApiOperation("批量上传员工照片")
  public R<AccountBatchImgDTO> uploadBatchImg(@RequestBody @Validated AccountBatchImgReq accountBatchImgReq) {
    accountBatchImgReq.getAccountBatchImgInfoReqList().forEach(c-> {
      if(c.getPhone().contains(".")) {
        c.setPhone(c.getPhone().substring(0, c.getPhone().indexOf(".")));
      }
    });

    AccountBatchImgDTO accountBatchImgDTO = accountService.uploadBatchImg(accountBatchImgReq);


    return success(accountBatchImgDTO);
  }

  @GetMapping("/syncOldData")
  @ApiOperation("员工账户增量查询")
  public R<List<AccountIncrementDTO>> incrementAccountList(@RequestParam(value = "staffStatus") Integer staffStatus){
    accountService.batchSyncData(staffStatus);
    return success();
  }


  @GetMapping("/increment")
  @ApiOperation("员工账户增量查询")
  public R<List<AccountIncrementDTO>> incrementAccountList(AccountIncrementReq accountIncrementReq){
    return success(accountService.queryIncrementDTO(accountIncrementReq));
  }

  @PostMapping("/page/merchant")
  @ApiOperation("商户端分页查询账户")
  public R<AccountPage<AccountDTO>> merchantPageQuery(@RequestBody
      AccountPageReq accountPageReq) {

    AccountPage<AccountDTO> accountDTOAccountPage = new AccountPage<>();

    if(accountPageReq.getAccountBalanceMin() != null && accountPageReq.getAccountBalanceMax() != null) {
      if(accountPageReq.getAccountBalanceMin().compareTo(accountPageReq.getAccountBalanceMax()) > 0) {
        return success(accountDTOAccountPage);
      }
    }

    if(accountPageReq.getSurplusQuotaMin() != null && accountPageReq.getSurplusQuotaMax() != null) {
      if(accountPageReq.getSurplusQuotaMin().compareTo(accountPageReq.getSurplusQuotaMax()) > 0) {
        return success(accountDTOAccountPage);
      }
    }

    Page<AccountPageDTO> page = new Page<>(accountPageReq.getCurrentPage(), accountPageReq.getPageSize());

    Page<AccountDTO> accountPage = accountService.getPageDTO(page, accountPageReq);
    accountDTOAccountPage.setRecords(accountPage.getRecords());
    accountDTOAccountPage.setTotal(accountPage.getTotal());
    accountDTOAccountPage.setSize(accountPage.getSize());
    accountDTOAccountPage.setCurrent(accountPage.getCurrent());
    if(!CollectionUtils.isEmpty(accountDTOAccountPage.getRecords())) {
      accountDTOAccountPage.setExt(accountService.getPageExtDTO(accountPageReq));
    }

    return success(accountDTOAccountPage);
  }

  @GetMapping("/page")
  @ApiOperation("分页查询账户")
  public R<Page<AccountDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      AccountPageReq accountPageReq) {
    Page<AccountPageDTO> page = new Page<>(currentPage, pageSize);
    Page<AccountDTO> accountPage = accountService.getPageDTO(page, accountPageReq);
    return success(accountPage);
  }

  @GetMapping("/{id}")
  @ApiOperation("员工账号详情")
  public R<AccountDetailDTO> detail(@PathVariable String id) {
    AccountDetailParam accountDetailParam = new AccountDetailParam();
    accountDetailParam.setId(Long.parseLong(id));
    return success(accountService.queryDetailByParam(accountDetailParam));
  }
  @GetMapping("/detailByPhone")
  @ApiOperation("通过手机号获取员工账号详情")
  public R<AccountDetailDTO> detailByPhone(@RequestParam @ApiParam("员工手机号")  String phone){
    AccountDetailParam accountDetailParam = new AccountDetailParam();
    accountDetailParam.setPhone(phone);
    return success(accountService.queryDetailByParam(accountDetailParam));
  }

  @GetMapping("/detailByAccountCode")
  @ApiOperation("通过账号获取员工账号详情")
  public R<AccountDetailDTO> detailByAccountCode(@RequestParam @ApiParam("员工账号")  String accountCode) {
    AccountDetailParam accountDetailParam = new AccountDetailParam();
    accountDetailParam.setAccountCode(Long.parseLong(accountCode));
    return success(accountService.queryDetailByParam(accountDetailParam));
  }

  @PostMapping("/save")
  @ApiOperation("新增员工账号")
  @MerchantUser
  @RepeatRequestVerification(prefixKey= "e-welfare-repeat-request:account_save")
  public R<Boolean> save(@RequestBody AccountReq accountReq) {
    try {
      accountReq.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
      return success(accountService.save(accountReq));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }


  @PostMapping("/update")
  @ApiOperation("修改员工账号")
  @MerchantUser
  public R<Boolean> update(@RequestBody AccountReq accountReq) {
    try {

      return success(accountService.update(accountReq));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/delete")
  @ApiOperation("删除员工账号")
  @MerchantUser
  public R<Boolean> delete(@RequestBody UpdateStatusReq updateStatusReq) {
    try {
      return success(accountService.delete(Long.parseLong(updateStatusReq.getId())));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }


  @PostMapping("/updateAccountStatus")
  @ApiOperation("激活或锁定账号")
  @MerchantUser
  public R<Boolean> updateAccountStatus(@RequestBody UpdateStatusReq updateStatusReq) {

    try {
      return success(accountService.active(Long.parseLong(updateStatusReq.getId()), updateStatusReq.getAccountStatus()));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @ApiOperation("员工账号导出")
  @GetMapping(value = "/exportAccount")
  public R<String> exportAccount(AccountPageReq accountPageReq) throws IOException {
    List<AccountDTO> accountDTOList = accountService.export(accountPageReq);
    String path = fileUploadService.uploadExcelFile(accountDTOList, AccountDTO.class, "员工账号");
    return success(fileUploadService.getFileServerUrl(path));
  }

  @ApiOperation("商户员工账号导出")
  @PostMapping(value = "/exportAccount/merchant")
  public R<String> exportMerchantAccount(@RequestBody AccountPageReq accountPageReq) throws IOException {
    List<AccountDTO> accountDTOList = accountService.export(accountPageReq);
    String path = fileUploadService.uploadExcelFile(accountDTOList, AccountDTO.class, "员工账号");
    return success(fileUploadService.getFileServerUrl(path));
  }

  @ApiOperation("批量新增员工账号")
  @PostMapping(value = "/uploadAccount")
  @MerchantUser
  public R<String> uploadAccount(
      @RequestPart(name = "file") @ApiParam(name = "file", required = true) MultipartFile multipartFile) {
    try {
      return success(accountService.uploadAccount(multipartFile));
    } catch (Exception be) {
      return R.fail(be.getMessage());
    }
  }

  @ApiOperation("批量绑卡")
  @PostMapping(value = "/batchBindCard")
  @MerchantUser
  public R<String> batchBindCard(@RequestParam(name = "file") MultipartFile multipartFile) {
    try {
      return success(accountService.accountBatchBindCard(multipartFile));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @GetMapping("/bill")
  @ApiOperation("员工账号消费汇总")
  public R<AccountBillDTO> queryBill(
      @RequestParam(required = false) @ApiParam("accountCode") String accountCode,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd) {
    return success(accountService.quertBill(accountCode, createTimeStart, createTimeEnd));
  }


  @GetMapping("/billDetail/page")
  @ApiOperation("员工账号消费明细")
  public R<Page<AccountBillDetailDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam @ApiParam("accountCode") String accountCode,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd) {
    Page<AccountBillDetailDTO> result = accountService
        .queryAccountBillDetail(currentPage, pageSize, accountCode, createTimeStart, createTimeEnd);
    return success(result);
  }

  @GetMapping("/account/bill/export")
  @ApiOperation("员工账号消费汇总导出")
  public R<String> exportAccountBill(@RequestParam @ApiParam("accountCode") String accountCode,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd) throws IOException {
    List<AccountBillDetailDTO> exportList = accountService
        .exportBillDetail(accountCode, createTimeStart, createTimeEnd);
    String path = fileUploadService.uploadExcelFile(exportList, AccountBillDetailDTO.class, "账户明细");
    return success(fileUploadService.getFileServerUrl(path));
  }
  @GetMapping("/account/binding")
  @ApiOperation("绑卡")
  public R<Boolean> bindingCard(@RequestParam @ApiParam("accountCode") String accountCode,
      @RequestParam @ApiParam("cardId") String cardId){
    try {
      return success(accountService.bindingCard(accountCode,cardId));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }


  @GetMapping("/detailByPhoneAndMerCode")
  @ApiOperation("通过手机号查询当前商户下的员工账号详情")
  @MerchantUser
  public R<AccountDetailDTO> detailByPhoneAndMer(@RequestParam(required = false) @ApiParam(value = "员工手机号")  String phone){
    return success(accountService.queryDetailPhoneAndMer(phone));
  }

  @GetMapping("/groupByDepartment")
  @ApiOperation("查询商户下组织机构树并统计各层级的人员数量")
  @MerchantUser
  public R<List<DepartmentAndAccountTreeResp>> groupByDepartment(){
    List<DepartmentAndAccountTreeResp> list = accountService.groupByDepartment(MerchantUserHolder.getMerchantUser().getMerchantCode());
    return success(list);
  }


  @PostMapping("/saveAndDeposit")
  @ApiOperation("新增账户并充值")
  public R<EmployerReqDTO> saveAndDeposit(@RequestBody AccountSaveAndDepositReq req) {
    return success(accountService.saveAndDeposit(req));
  }
}