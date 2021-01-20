package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.AccountTypeMapperDTO;
import com.welfare.persist.dto.MerSupplierStoreDTO;
import com.welfare.persist.dto.query.AccountTypeReq;
import com.welfare.persist.entity.AccountType;
import com.welfare.service.AccountTypeService;
import com.welfare.service.converter.AccountTypeConverter;
import com.welfare.service.dto.AccountTypeDTO;
import com.welfare.servicemerchant.dto.UpdateStatusReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 19:46
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountType")
@Api(tags = "员工类型管理")
public class AccountTypeController implements IController {

  @Autowired
  private AccountTypeService accountTypeService;
  @Autowired
  private AccountTypeConverter accountTypeConverter;

  @GetMapping("/page")
  @ApiOperation("分页员工类型列表")
  public R<Page<AccountTypeDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户代码") String merCode,
      @RequestParam(required = false) @ApiParam("类型编码") String typeCode,
      @RequestParam(required = false) @ApiParam("类型名称") String typeName,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    Page<AccountTypeMapperDTO> page = new Page(currentPage,pageSize);
    Page<AccountTypeDTO> accountTypePage = accountTypeService.getPageDTO(page, merCode,typeCode,
        typeName,createTimeStart,createTimeEnd);
    return success(accountTypePage);
  }

  @GetMapping("/list")
  @ApiOperation("查询商户下所有的员工类型list")
  public R<List<AccountTypeDTO>> queryAccountTypeDTO(@RequestParam(required = false) @ApiParam("商户代码") String merCode){
    return success(accountTypeService.queryAccountTypeDTO(merCode,null,null,null,null));
  }

  @GetMapping("/merSupplierStore/list")
  @ApiOperation("查询商户下所有的供应商门店list")
  public R<List<MerSupplierStoreDTO>> queryMerSupplierStoreDTList(@RequestParam @ApiParam("商户代码") String merCode){
    return success(accountTypeService.queryMerSupplierStoreDTList(merCode));
  }




  @GetMapping("/{id}")
  @ApiOperation("员工类型详情")
  public R<AccountType> detail(@PathVariable String id){
    try {
      return success(accountTypeService.getAccountType(Long.parseLong(id)));
    }catch (BusiException be){
      return R.fail(be.getMessage());
    }

  }

  @PostMapping("/save")
  @ApiOperation("新增员工类型")
  @MerchantUser
  public R<Boolean> save(@RequestBody AccountTypeReq accountTypeReq){
    try{
      AccountType accountType = accountTypeConverter.toEntity(accountTypeReq);
      accountType.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
      return success(accountTypeService.save(accountType));
    }catch (BusiException be){
      return R.fail(be.getMessage());
    }

  }

  @PostMapping("/update")
  @ApiOperation("修改员工类型")
  @MerchantUser
  public R<Boolean> update(@RequestBody AccountTypeReq accountTypeReq){
    try {
      AccountType accountType = accountTypeConverter.toEntity(accountTypeReq);
      accountType.setUpdateUser(MerchantUserHolder.getMerchantUser().getUsername());
      return success(accountTypeService.update(accountType));
    }catch (BusiException be){
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/delete")
  @ApiOperation("删除员工类型")
  public R<Boolean> delete(@RequestBody UpdateStatusReq updateStatusReq){
    try {
      return success(accountTypeService.delete(Long.parseLong(updateStatusReq.getId())));
    }catch (BusiException be){
      return R.fail(be.getMessage());
    }
  }

}
