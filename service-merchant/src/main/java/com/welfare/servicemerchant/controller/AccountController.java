package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.AccountService;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountPageReq;
import com.welfare.service.dto.AccountReq;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDepositApplyInfo;
import com.welfare.service.dto.AccountBillDetailDTO;
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


  @GetMapping("/incremental-page")
  @ApiOperation("增量查询账户(支持离线消费场景)")
  public R<Page<AccountDepositApplyInfo>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
                                                    @RequestParam @ApiParam("单页大小") Integer pageSize,
                                                    @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                                    @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }

  @GetMapping("/page")
  @ApiOperation("分页查询账户")
  public R<Page<AccountDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      AccountPageReq accountPageReq) {
       Page<AccountPageDTO> page = new Page(currentPage,pageSize);
        Page<AccountDTO> accountPage = accountService.getPageDTO(page,accountPageReq);
        return success(accountPage);
  }

  @GetMapping("/{id}")
  @ApiOperation("员工账号详情")
  public R<AccountDetailDTO> detail(@PathVariable Long id){
    return success(accountService.queryDetail(id));
  }

  @PostMapping("/save")
  @ApiOperation("新增员工账号")
  public R<Boolean> save(@RequestBody AccountReq accountReq){
    Account account = new Account();
    BeanUtils.copyProperties(accountReq,account);
    return success(accountService.save(account));
  }


  @PostMapping("/update")
  @ApiOperation("修改员工账号")
  public R<Boolean> update(@RequestBody AccountReq accountReq){
    Account account = new Account();
    BeanUtils.copyProperties(accountReq,account);
    return success(accountService.update(account));
  }

  @PostMapping("/delete/{id}")
  @ApiOperation("删除员工账号")
  public R<Boolean> delete(@PathVariable Long id){
    return success(accountService.delete(id));
  }


  @PostMapping("/active/{id}")
  @ApiOperation("激活或锁定账号")
  public R<Boolean> active(@PathVariable Long id,@RequestParam @ApiParam("状态") Integer active){
    return success(accountService.active(id,active));
  }

  @ApiOperation("员工账号导出")
  @GetMapping(value="/exportAccount")
  public R<String> exportAccount(AccountPageReq accountPageReq) throws IOException {
    List<AccountDTO>  accountDTOList = accountService.export(accountPageReq);
    String path = fileUploadService.uploadExcelFile(accountDTOList, AccountDTO.class, "员工账号");
    return success(fileUploadService.getFileServerUrl(path));
  }

  @ApiOperation("批量新增员工账号")
  @PostMapping(value = "/uploadAccount")
  public R<String> uploadAccount(@RequestPart(name = "file")@ApiParam(name = "file",required = true)MultipartFile multipartFile) {
    return success(accountService.uploadAccount(multipartFile));
  }
  @ApiOperation("批量绑卡")
  @PostMapping(value = "/batchBindCard")
  public R<String> batchBindCard(@RequestParam(name = "file") MultipartFile multipartFile) {
    return success(accountService.accountBatchBindCard(multipartFile));
  }

  @GetMapping("/bill")
  @ApiOperation("员工账号消费汇总")
  public R<AccountBillDTO> quertBill(@RequestParam(required = false) @ApiParam("accountCode") String accountCode,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    return success(accountService.quertBill(accountCode,createTimeStart,createTimeEnd));
  }


  @GetMapping("/billDetail/page")
  @ApiOperation("员工账号消费明细")
  public R<Page<AccountBillDetailDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam @ApiParam("accountCode") String accountCode,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    Page<AccountBillDetailDTO> result = accountService.queryAccountBillDetail(currentPage,pageSize,accountCode,createTimeStart,createTimeEnd);
    return success(result);
  }

  @GetMapping("/account/bill/export")
  @ApiOperation("员工账号消费汇总导出")
  public R<String> exportAccountBill(@RequestParam @ApiParam("accountCode") String accountCode,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd) throws IOException{
    List<AccountBillDetailDTO> exportList = accountService.exportBillDetail(accountCode,createTimeStart,createTimeEnd);
    String path =fileUploadService.uploadExcelFile(exportList, AccountBillDetailDTO.class, "账户明细");
    return success(fileUploadService.getFileServerUrl(path));
  }
}