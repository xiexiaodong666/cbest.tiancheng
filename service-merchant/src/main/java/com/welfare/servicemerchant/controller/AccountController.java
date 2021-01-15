package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.exception.BusiException;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.AccountService;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountIncrementReq;
import com.welfare.service.dto.AccountPageReq;
import com.welfare.service.dto.AccountReq;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/6  5:16 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Api(tags = "员工账号管理")
public class AccountController implements IController {

  @Autowired
  private AccountService accountService;
  @Autowired
  private FileUploadService fileUploadService;


  @GetMapping("/increment")
  @ApiOperation("员工账户增量查询")
  public List<AccountIncrementDTO> incrementAccountList(AccountIncrementReq accountIncrementReq){
    return accountService.queryIncrementDTO(accountIncrementReq);
  }

  @GetMapping("/page")
  @ApiOperation("分页查询账户")
  public R<Page<AccountDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      AccountPageReq accountPageReq) {
    Page<AccountPageDTO> page = new Page(currentPage, pageSize);
    Page<AccountDTO> accountPage = accountService.getPageDTO(page, accountPageReq);
    return success(accountPage);
  }

  @GetMapping("/{id}")
  @ApiOperation("员工账号详情")
  public R<AccountDetailDTO> detail(@PathVariable String id) {
    return success(accountService.queryDetailByParam(Long.parseLong(id),null,null));
  }
  @GetMapping("/detailByPhone")
  @ApiOperation("员工账号详情")
  public R<AccountDetailDTO> detailByPhone(@RequestParam @ApiParam("员工手机号")  String phone){
    return success(accountService.queryDetailByParam(null,null,phone));
  }

  @GetMapping("/detailByAccountCode")
  @ApiOperation("通过账号获取员工账号详情")
  public R<AccountDetailDTO> detailByAccountCode(@RequestParam @ApiParam("员工账号")  String accountCode) {
    return success(accountService.queryDetailByParam(null,Long.parseLong(accountCode),null));
  }

  @PostMapping("/save")
  @ApiOperation("新增员工账号")
  public R<Boolean> save(@RequestBody AccountReq accountReq) {
    try {
      Account account = new Account();
      BeanUtils.copyProperties(accountReq, account);
      account.setStoreCode(accountReq.getDepartmentCode());
      return success(accountService.save(account));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }


  @PostMapping("/update")
  @ApiOperation("修改员工账号")
  public R<Boolean> update(@RequestBody AccountReq accountReq) {
    try {
      Account account = new Account();
      BeanUtils.copyProperties(accountReq, account);
      return success(accountService.update(account));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/delete/{id}")
  @ApiOperation("删除员工账号")
  public R<Boolean> delete(@PathVariable String id) {
    try {
      return success(accountService.delete(Long.parseLong(id)));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }


  @PostMapping("/active/{id}")
  @ApiOperation("激活或锁定账号")
  public R<Boolean> active(@PathVariable String id, @RequestParam @ApiParam("状态") Integer active) {

    try {
      return success(accountService.active(Long.parseLong(id), active));
    } catch (BusiException be) {
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

  @ApiOperation("批量新增员工账号")
  @PostMapping(value = "/uploadAccount")
  public R<String> uploadAccount(
      @RequestPart(name = "file") @ApiParam(name = "file", required = true) MultipartFile multipartFile) {
    try {
      return success(accountService.uploadAccount(multipartFile));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @ApiOperation("批量绑卡")
  @PostMapping(value = "/batchBindCard")
  public R<String> batchBindCard(@RequestParam(name = "file") MultipartFile multipartFile) {

    try {
      return success(accountService.accountBatchBindCard(multipartFile));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @GetMapping("/bill")
  @ApiOperation("员工账号消费汇总")
  public R<AccountBillDTO> quertBill(
      @RequestParam(required = false) @ApiParam("accountCode") String accountCode,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
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
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd) throws IOException {
    List<AccountBillDetailDTO> exportList = accountService
        .exportBillDetail(accountCode, createTimeStart, createTimeEnd);
    String path = fileUploadService.uploadExcelFile(exportList, AccountBillDetailDTO.class, "账户明细");
    return success(fileUploadService.getFileServerUrl(path));
  }
}